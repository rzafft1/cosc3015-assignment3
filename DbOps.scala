

object testing
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

// PROJECT /////////////////////////////////////////////////////////////////////
def project(table: Table, colPred: (Int) => Boolean) : Table =
{
    def getCol (col_index: List[Int], row: Tuple): Tuple = 
    {
        col_index match
            case Nil => EmptyTuple
            case first :: rest => if colPred(first) then row.productElement(first) *: getCol(rest,row) else getCol(rest,row)
    }
    def getIndices(list: Tuple, i: Int): List[Int] = 
    {
        list match 
            case EmptyTuple => Nil
            case head*:tail => i :: getIndices((tail), i+1)
    }
    table match 
        case Nil => Nil
        case head :: tail => getCol(getIndices(head,0),head) :: project(tail, colPred)
}

// SELECT //////////////////////////////////////////////////////////////////////
def select(table: Table, rowPred: (Tuple) => Boolean) : Table =
{
    table match
        case Nil => Nil
        case head :: tail if rowPred(head) => head :: select(tail, rowPred)
        case _ :: tail => select(tail, rowPred)
}

// JOIN ////////////////////////////////////////////////////////////////////////
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

// QUERIES /////////////////////////////////////////////////////////////////////

def selectRows[A](idx: Int, pred: (A) => Boolean): (Tuple) => Boolean = (row) => pred(row.productElement(idx).asInstanceOf[A])
def equiJoin[A](idx1:Int, idx2:Int): (Tuple, Tuple) => Boolean = (left, right) => left.productElement(idx1) == right.productElement(idx2)


// testing /////////////////////////////////////////////////////////////////////
println("project test 1 -> keep cols 1 & 2         : " + project(emps, col => col == 1 || col == 2))
println("project test 1 -> keep all cols exept 2   : " + project(emps, col => col != 3))
println("select test 1  -> select age == 21        : " + select(emps, row => row.productElement(2).asInstanceOf[Int] == 21))
println("select test 2  -> select salary > 100,000 : " + select(emps, row => row.productElement(3).asInstanceOf[Double] > 90000))
println("select test 3  -> select name == ryan     : " + select(emps, row => row.productElement(1).asInstanceOf[String] == "Ryan"))
println("join test 1    -> match id's              : " + join(depts, emps, (L, R) => L.productElement(0).asInstanceOf[Int] == R.productElement(0).asInstanceOf[Int]))

println("select test 0  -> select name == ryan     : " + select(emps, selectRows(1, x => x == "Ryan")))
println("join test 0    -> match id's              : " + join(depts, emps, equiJoin(0,0)))




}
}
