val hello = "Bonjour!"
println(hello)

var helloThere: String = hello
helloThere = hello + " There!"
println(helloThere)

val immutableHelloThere = hello + " There!"
val moreStuff = immutableHelloThere + " year"

val pi: Double = 3.14159265
val piSinglePrecision: Float = 3.14159f
val bigNumber: Long = 12345678901
val smallNumber: Byte = 127