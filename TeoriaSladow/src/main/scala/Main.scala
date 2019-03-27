object Main {

  def parseTransactionLeft(transaction: String): Char = {
    //parsowanie lewej strony transakcji
    val transactionLeft: String = transaction.split('=')(0)

    val anyVariable = "[a-zA-z]"
    val variables: List[Char] = anyVariable.r.findAllMatchIn(transactionLeft).toList.map(a => a.toString().head)

    if (variables.size != 1) {
      println("relacja powinna zawierac tylko jedna zmienna po lewej stronie")
      System.exit(1)
    }

    variables.head
  }

  def parseTransactionRight(transaction: String): List[Char] = {
    //parsowanie prawej strony transakcji
    val transactionRight: String = transaction.split('=')(1)

    val anyVariable = "[a-zA-z]"
    val variables: List[Char] = anyVariable.r.findAllMatchIn(transactionRight).toList.map(a => a.toString().head)

    variables
  }

  def dependentTransactions(pairOfLetters: (Char, Char), transactions: List[(Char, Char, List[Char])]): Boolean = {
    val (letter1, letter2) = pairOfLetters

    //transakcja jest zawsze zalezna od siebie
    if (letter1 == letter2)
      return true

    //szukanie transakcji odpowiadajacych swoim literom w alfabecie
    var transaction1: (Char, Char, List[Char]) = null
    var transaction2: (Char, Char, List[Char]) = null

    for (transaction <- transactions) {
      if (transaction._1 == letter1)
        transaction1 = transaction

      if (transaction._1 == letter2)
        transaction2 = transaction
    }

    //sprawdzenie warunkow wystarczajacych zaleznosci transakcji
    if (transaction1._3.contains(transaction2._2))
      return true

    if (transaction2._3.contains(transaction1._2))
      return true

    if (transaction1._2 == transaction2._2)
      return true

    false
  }

  def enumerateOperations(word: List[Char]): List[String] = {
    //ponumerowanie operacji tak, by kolejne jej wystąpienia w slowie mialy wyzsze numery niz poprzednie
    var operations: List[String] = Nil

    for (i <- word.indices) {
      val operation = word(i)
      var counter = 0
      for (j <- 0 until i)
        if (word(j) == operation)
          counter += 1
      operations = operations ++ List(operation.toString ++ counter.toString)
    }

    operations
  }

  def calculateFC(operations: List[String], relationOfDependency: List[(Char, Char)]): List[String] = {

    if (operations == Nil)
      return Nil

    val operation = operations.last
    //przefiltrowanie operacji tak, by zostały te od których operation jest zalezne
    val depententOperations = operations.filter(a => relationOfDependency.contains((operation.head, a.head)))

    //jesli nie znajde operacji od krotych zalezne jest operation to dodaje je do klasy foaty
    //i szukam dalej jej czlonkow wsrod poprzedzajacych operation operacji
    if (depententOperations == List(operation))
      return calculateFC(operations.dropRight(1), relationOfDependency) ++ List(operation)

    //w innym przypadku nie dodaje operation do klasy foaty
    calculateFC(operations.dropRight(1), relationOfDependency)

  }

  def calculateFNF(operations: List[String], relationOfDependency: List[(Char, Char)]): List[List[String]] = {

    if (operations == Nil)
      return Nil

    //wyliczamy klase Foaty
    val firstFC = calculateFC(operations, relationOfDependency)

    //dodajemy ja na poczatek postaci normalnej i wyliczamy dalej dla operacji nie bedacych w wyliczonej klasie
    firstFC :: calculateFNF(operations.filter(a => !firstFC.contains(a)), relationOfDependency)

  }

  def checkIfConnected(edges: List[(String, String)], left: List[String], right: String): Boolean = {
    if (left.isEmpty)
      return false

    if (left.contains(right))
      return true

    //jesli nie doszlismy do celu wrzucamy jako zrodlo wszystkie wierzcholki do ktorych bezposrednio dochodzimy ze zrodla
    var newLeft: List[String] = Nil
    for (vertex <- left) {
      for (edge <- edges.filter(a => a._1 == vertex)) {
        newLeft = edge._2 :: newLeft
      }
    }
    checkIfConnected(edges, newLeft, right)
  }

  def minimizeGraph(edges: List[(String, String)]): List[(String, String)] = {

    for (edge <- edges) {
      val (left, right) = edge
      //dla każdej krawedzi sprawdzenie czy po wyrzuceniu danej krawedzi z grafu jej konce wciaz beda polaczone
      if (checkIfConnected(edges.filter(a => a != edge), List(left), right))
        return minimizeGraph(edges.filter(a => a != edge))
    }

    edges
  }

  def generateGraph(operations: List[String], relationOfDependency: List[(Char, Char)]): List[(String, String)] = {

    var edges: List[(String, String)] = Nil

    //wrzucenie do grafu wszystkich krawedzi ktore lacza zalezne operacje i prowadzą w prawo w sladzie
    for (i <- operations.indices) {
      val operation = operations(i)
      for (j <- i + 1 until operations.size) {
        if (relationOfDependency.contains((operation.head, operations(j).head))) {
          edges = (operation, operations(j)) :: edges
        }
      }
    }

    //minimalizacja grafu
    minimizeGraph(edges)
  }

  def generateFNFfromGraph(verticles: List[String], edges: List[(String, String)]): List[List[String]] = {
    if (verticles.isEmpty)
      return Nil

    val rightSidesOfEdges = edges.map(a => a._2)
    //wrzucenie do klasy Foaty wierzcholkow do ktorych nie dochodza krawedzie
    val foataClass = verticles.filter(a => !rightSidesOfEdges.contains(a))

    //wyrzucenie z grafu krawedzi wychodzacych z powyzszych wierzcholkow
    val newEdges = edges.filter(a => !foataClass.contains(a._1))

    //szukanie klas Foaty w pozostalym grafie
    foataClass :: generateFNFfromGraph(rightSidesOfEdges, newEdges)
  }

  def main(args: Array[String]): Unit = {

    println("wpisz rozmiar alfabetu")
    val sizeOfAlphabet = scala.io.StdIn.readInt()

    var alphabet: List[Char] = Nil
    var transactionsLeft: List[Char] = Nil
    var transactionsRight: List[List[Char]] = Nil
    var transactions: List[(Char, Char, List[Char])] = Nil

    //parsowanie transakcji
    for (_ <- 1 to sizeOfAlphabet) {
      println("wpisz znak alfabetu")
      alphabet = scala.io.StdIn.readChar() :: alphabet

      println("wpisz transkcje w postaci a = b + c odpowiadającą temu znakowi")
      val transaction: String = scala.io.StdIn.readLine()
      transactionsLeft = parseTransactionLeft(transaction) :: transactionsLeft
      transactionsRight = parseTransactionRight(transaction) :: transactionsRight

      transactions = (alphabet.head, transactionsLeft.head, transactionsRight.head) :: transactions
    }

    //wyznaczanie relacji zaleznosci i niezaleznosci
    val pairsOfLetters = alphabet.flatMap(a => alphabet.map(b => (a, b)))
    val relationOfDependency = pairsOfLetters.filter(dependentTransactions(_, transactions))
    val relationOfIndependency = pairsOfLetters.filter(!dependentTransactions(_, transactions))

    //wypisywanie relacji zaleznosci i niezaleznosci
    println("\nRelacja zaleznosci:\nD = {")
    for ((a, b) <- relationOfDependency) {
      println("(" + a + ", " + b + "),")
    }
    println("}")

    println("\nRelacja niezaleznosci:\nI = {")
    for ((a, b) <- relationOfIndependency) {
      println("(" + a + ", " + b + "),")
    }
    println("}")

    //parsowanie slowa podanego na wejsciu
    println("\nWpisz slowo - ciag znakow z alfabetu")
    val w: String = scala.io.StdIn.readLine()
    val anyVariable = "[a-zA-z]"
    val word: List[Char] = anyVariable.r.findAllMatchIn(w).toList.map(a => a.toString().head)
    var operations = enumerateOperations(word)

    //wyznaczanie postaci normalnej foaty na podstawie relacji zaleznosci
    val foataNF = calculateFNF(operations, relationOfDependency)

    //wypisanie FNF
    print("FNF([w]) = ")
    for (foataClass <- foataNF) {
      print("( ")
      for (operation <- foataClass) {
        print(operation + " ")
      }
      print(") ")
    }

    //wygenerowanie grafu Diekerta
    val edges = generateGraph(operations, relationOfDependency)

    //wypisanie grafi Diekerta
    println("\n\ndigraph g{")
    for (edge <- edges) {
      val (left, right) = edge
      println(left + " -> " + right)
    }
    for (operation <- operations) {
      println(operation + " [label = " + operation.head + "]")
    }
    println("}")

    //wygenerowanie FNF na podstawie grafu Diekerta
    val foataNF2 = generateFNFfromGraph(operations, edges)

    //wypisanie FNF
    println("\nFNF na podstawie grafu:")
    print("FNF([w]) = ")
    for (foataClass <- foataNF2) {
      print("( ")
      for (operation <- foataClass) {
        print(operation + " ")
      }
      print(") ")
    }
    print("\n")

  }

}
