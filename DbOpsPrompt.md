In this assignment you will implement a simple execution engine for a database. Specifically, this assignment should demonstrate that you know exactly how to write functions that take functions as parameters, and also functions that create new functions and return them.

Start by following the instructions in Assignment #1: Getting Started with Scala to create a new empty project. Then create a package called "base" and the file "DbOps.scala".

Databases operate on tables, which are like simple spreadsheets. We often talk about a table as being a collection of rows, and each row has the same number of columns and the same types for the columns.

The actual database engine will work for any table, not just one of the tables above. So we need a generic "Table" data type with an unknown number of columns and types.  Scala has the type Tuple that works perfectly for this purpose. So from now on, think of a table as list of tuples. You can use the method Tuple.fromProductTyped(x) to convert a value from a case class into a tuple. So your first task should be to write some code to convert those tables with well-defined types to more generic lists of tuples. Here is the generic table type.

type Table = List[Tuple]
Now you can implement generic databases operations that operate on the Table data type (i.e., that can work on any table, not just a table of employees.) 
def project(table: Table, colPred: (Int) => Boolean) : Table = ???
def select(table: Table, rowPred: (Tuple) => Boolean) : Table = ???
def join(lhs: Table, rhs: Table, theta: (Tuple, Tuple) => Boolean) : Table = ???
The projection function returns a new table that has all the rows as the old table, but only the columns that pass the test given by colPred. Notice that colPred is a function that returns true or false when given a column index. E.g., if colPred(1) is true, then the second column (0-offset, right?) will be included in the returned tuples. The selection function returns a subset of the rows in the table, just those rows that pass the test. Again, pay attention to the argument types. Finally, the join function takes in two tables and returns a single table that has all the columns from each of the tables. A row is included in the result if the "theta" condition is true for the pair of tuples. (Again, look at the parameter types.) Note that join should consider all possible combinations of rows from the first and second table, so if you join a table with 4 elements with another table with 7 elements, the result could have as many as 28 rows.

You now have the basics of a database engine, and we could start executing queries. But to use your current engine, you would have to write a bunch of very similar functions, e.g., to project the first and third column, or select the rows with third column greater than 25, or whatever. So let's make that simpler. So write the following helper functions:

def projectCols(list:List[Int]): (Int) => Boolean = ???
def selectRows[A](idx:Int, pred: (A) => Boolean): (Tuple) => Boolean = ???
def equiJoin[A](idx1:Int, idx2:Int): (Tuple, Tuple) => Boolean = ???
def andConds(preds: List[(Tuple) => Boolean]): (Tuple) => Boolean = ???
def orConds(preds: List[(Tuple) => Boolean]): (Tuple) => Boolean = ???
Pay close attention to the types of those functions. You may not modify those types, though I will let you use Curried functions to achieve the same effect. That's up to you.

The projectCols function takes in a list of integers, and returns a function suitable for the project function that shows only the columns in the list.
The selectRows function takes in a column index and a value, and selects the row (or rows) that have that value for that specific column. Note the type parameter [A] is used to refer to the data type of that particular column. Again, pay attention to the types in this code!
The equiJoin function is special. A general "theta" join uses any condition on two tuples. The equijoins are restricted in that the only condition you can specify is this column of the first tuple is equal to that column of the other tuple. 
The orConds and andConds functions let you combine multiple conditions with boolean operators.  Note: I'm not asking you to do this, but think about the fact that some conditions are more likely to be true than others. E.g., x>=100 is more likely than x==100, right? Since all these pieces are functional, a real database engine would "optimize" the query by rearranging the order of the conditions in the ANDs and ORs (and the joins, and a host of other things covered in the database course.) This only works because of immutability! Databases use two languages for most of their work: a declarative language called SQL, and a functional language called "relational algebra." As a database practitioner, you have to be familiar with both of these. In addition, some databases support an imperative language, e.g., PL/SQL or Transact/SQL. That's usually reserved for database administrators who really know what they're doing, because those languages effectively bypass the optimization engine and leave optimization up to the programmer (no immutability, so the optimizer can't do its job very well.)
When you're done, create a single expression using the functions you just defined, and defining no more functions (not even anonymous functions) to find the name and department of all employees who are older than 25 and work in Bedrock.

Handy Hints

You can program with tuples very much like you program with lists, except
Use *: instead of ::
Use ++ instead of :::
Use EmptyTuple instead of Nil
Don't forget that you can convert an instance of a case class into a Tuple with Tuple.fromProductTyped(x)
To get the nth element of a Tuple, use tup.productElement(n). In particular, tup(n) does NOT work.
To convert an object to another type (i.e., to cast the type of an object) use the method x.asInstanceOf[Int] (or whatever type you want.)
Be very careful with your types!!!
