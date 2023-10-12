
object DbOps.scala
{
def main (Args: Array[String]) = 
{

case class Employee(id: Int, first: String, age: Int, salary: Double)
case class Department(dept_id: Int, dept: String, city: String)
case class EmployeeDepartment(id: Int, dept_id: Int)

val depts = List(
    Tuple.fromProductTyped(Department(1, "Transportation", "Laramie")),
    Tuple.fromProductTyped(Department(2, "Sales", "Denver")),
    Tuple.fromProductTyped(Department(2, "Abacus", "Austin"))
)

val emps = List(
    Tuple.fromProductTyped(Employee(1, "Barney", 30, 15000.0)),
    Tuple.fromProductTyped(Employee(2, "Betty", 28, 125000.0)),
    Tuple.fromProductTyped(Employee(3, "Wilma", 21, 95000.0)),
    Tuple.fromProductTyped(Employee(4, "Fred", 29, 75000.0)),
    Tuple.fromProductTyped(Employee(5, "Ryan", 21, 150000.0)),
    Tuple.fromProductTyped(Employee(6, "Erik", 23, 95000.0)),
    Tuple.fromProductTyped(Employee(7, "Zack", 20, 18000.0)),
    Tuple.fromProductTyped(Employee(8, "Julian", 19, 56000.0)),
    Tuple.fromProductTyped(Employee(9, "Bobby", 23, 43000.0)),
    Tuple.fromProductTyped(Employee(10, "Jim", 19, 39000.0)),
    Tuple.fromProductTyped(Employee(11, "Tommy", 18, 67000.0)),
    Tuple.fromProductTyped(Employee(12, "Wanda", 22, 71000.0)),
    Tuple.fromProductTyped(Employee(2, "Ryan", 20, 82000.0)),
)

type Table = List[Tuple]

def project (table: Table, colPred: (Int) => Boolean): Table = 
{
    def editRow (row: Tuple, n: Int): Tuple = 
    {
        row match 
            case EmptyTuple => EmptyTuple
            case head *: tail if colPred(n) => tail
            case head *: tail => head *: editRow(tail, n + 1)
    }
    table match
        case Nil => Nil // Handle the case when the list is empty
        case head :: tail => editRow(head.asInstanceOf[Tuple], 0) :: project(tail, colPred)
}

def select(table: Table, rowPred: (Tuple) => Boolean) : Table =
{
    table match
        case Nil => Nil
        case head :: tail if rowPred(head) => head :: select(tail, rowPred)
        case _ :: tail => select(tail, rowPred)
}


def join(lhs: Table, rhs: Table, theta: (Tuple, Tuple) => Boolean): Table =
{
    def joinHead(lhsHead: Tuple, rhs: Table): Table = 
    {
        rhs match 
            case Nil => Nil
            case rhsHead :: rhsTail if theta(lhsHead, rhsHead) => (lhsHead, rhsHead) :: joinHead(lhsHead, rhsTail)
            case _ :: rhsTail => joinHead(lhsHead, rhsTail)
    }
    lhs match 
        case Nil => Nil
        case lhsHead :: lhsTail => joinHead(lhsHead, rhs) ::: join(lhsTail, rhs, theta)
}

def selectRows[A](idx: Int, pred: (A) => Boolean): (Tuple) => Boolean = (row) => pred(row.productElement(idx).asInstanceOf[A])
def equiJoin[A](idx1:Int, idx2:Int): (Tuple, Tuple) => Boolean = (left, right) => left.productElement(idx1) == right.productElement(idx2)


println("project test 1 -> remove column 1         : " + project(emps, cond => cond == 1))
println("select test 1  -> select age == 21        : " + select(emps, row => row.productElement(2).asInstanceOf[Int] == 21))
println("select test 2  -> select salary > 100,000 : " + select(emps, row => row.productElement(3).asInstanceOf[Double] > 90000))
println("select test 3  -> select name == ryan     : " + select(emps, row => row.productElement(1).asInstanceOf[String] == "Ryan"))
println("join test 1    -> match id's              : " + join(depts, emps, (L, R) => L.productElement(0).asInstanceOf[Int] == R.productElement(0).asInstanceOf[Int]))

println("select test 0  -> select name == ryan     : " + select(emps, selectRows(1, (x: String) => x == "Ryan")))
println("join test 0    -> match id's              : " + join(depts, emps, equiJoin(0,0)))


}
}
