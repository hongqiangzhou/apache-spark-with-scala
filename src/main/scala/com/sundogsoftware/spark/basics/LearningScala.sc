val hello = "Bonjour!"
println(hello)

var helloThere: String = hello
helloThere = hello + " There!"
println(helloThere)

val immutableHelloThere = hello + " There!"
val moreStuff = immutableHelloThere + " year"

val numberOne: Int = 1
val truth: Boolean = true
val letterA: Char = 'a'
val pi: Double = 3.14159265
val piSinglePrecision: Float = 3.14159f
val bigNumber: Long = 1234567890
val smallNumber: Byte = 127

println(f"Pi is about $piSinglePrecision%.3f")
println(f"Zero padding on the left: $smallNumber%05d")
println(s"I can use the s prefix to use variables like $numberOne $truth $letterA")
println(s"The s prefix isn't limited to variables; I can include any expression. Like ${1 + 2}")

val theUltimateAnswer = "To life, the universe, and everything is 42."
val pattern = """.* ([\d]+).*""".r
val pattern(answerString) = theUltimateAnswer
println(answerString.toInt)

val number = 3
number match {
  case 1 => println("One")
  case 2 => println("Two")
  case 3 => println("Three")
  case _ => println("Something else")
}

for (x <- 1 to 4) {
  val squared = x * x
  println(squared)
}

var x = 10
while (x >= 0) {
  println(x)
  x -= 1
}

x = 0
do {
  println(x);
  x += 1
} while (x <= 10)

println({val x = 10; x + 20})

def cubeIt(x: Int): Int = {x * x * x}
println(cubeIt(10))

def transformInt(x: Int, f: Int => Int): Int = f(x)
val result = transformInt(2, cubeIt)
transformInt(2, x => x * x)

// Fabinaci
/*def fabonacci(n: Int): Int = {
  n match{
    case 0 | 1 => 1
    case _ => fabonacci(n - 1) + fabonacci(n - 2)
  }
}*/
def fabonacci(n: Int): Int = {
  def tail(n: Int, a: Int, b: Int): Int = n match {
    case 0 => a
    case _ => tail(n - 1, b , a + b)
  }
  tail(n, 0, 1)
}

fabonacci(4)

val captainStuff = ("Picard", "Enterprise-D", "NCC-1701-D")
println(captainStuff._1)

val picardsShip = "Picard" -> "Enterprise-D"
println(picardsShip._1)
println(picardsShip._2)

val aBunchOfStuff = ("Kirk", 1964, true)

val shipList = List("Enterprise", "Defiant", "Voyager", "Deep Space Nine")
println(shipList(1))
println(shipList.head)
println(shipList.tail)
for (ship <- shipList) {
  println(ship)
}

val backwardShips = shipList.map((ship: String) => {ship.reverse})

val numberList = List(1, 2, 3, 4, 5)
val sum = numberList.reduce((x: Int, y: Int) => x + y)