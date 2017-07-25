/* this code finds the pearson coefficient from the file cor.txt which contains pairs of doubles (x,y) */

/*this line is for loading data from disk and storing in the RDD (lines). Note that textFile() reads one line at a time */
val lines = sc.textFile("cor.txt")

/* Here we use map to transform the list of string lines to a list of tuples containing x,y and x*y */
/* Where x is the 1st word in the string converted to double and y is the second word converted to double */
/* So, each element in XYpair is ( x , y , x*y ) */
val XYpair = lines.map{ line => (line.split("\\s+")(0).toDouble , line.split("\\s+")(1).toDouble , line.split("\\s+")(0).toDouble*line.split("\\s+")(1).toDouble ) }

/* Here we use map to transform the list of lines to a list of tuples containing x^2,and y^2 */
/* So, each element in XYpairsq is ( x^2 , y^2 ) */
val XYpairsq = lines.map{ line => (Math.pow(line.split("\\s+")(0).toDouble,2) , Math.pow(line.split("\\s+")(1).toDouble,2) )}
/* Total number of x,y pair or the number of elements in list XYpair */
var n = XYpair.count()

println("Total Number of X,Y pair read : ")
println(n)

/* Now, we reduce the above two lists by simply adding up all the elements into the following two tuples */
/* sum is a tuple : (sum of all xi, sum of all yi, sum of all xi * yi ) */
/* sum : ( sum(xi) , sum(yi) , sum(xi*yi) ) */
val sum = XYpair.reduce{ (x,y) => (x._1 + y._1 , x._2 + y._2, x._3 + y._3 )  }
/* sumsq is a tuple : ( sum of all xi^2, sum of all yi^2 ) */
/* sumsq : ( sum(xi^2) , sum(yi^2) ) */
val sumsq = XYpairsq.reduce{ (x,y) => (x._1 + y._1 , x._2 + y._2)  }


//We follow the following formula to calculate the pearson's coefficient:

//       										n*sum(xi*yi) - sum(xi)*sum(yi) 
//		pearson coeffiecnt  =	--------------------------------------------------------------
//									sqrt(n*sum(xi^2)-sum(xi)^2)*sqrt(n*sum(yi^2)-sum(yi)^2)


//Now since we have all the required data, we calculate pearson's coefficient as follows :
//The numerator
var numerator = n*sum._3 - sum._1*sum._2;

//The denominator
var denominator = Math.sqrt(n*sumsq._1-Math.pow(sum._1,2)) * Math.sqrt(n*sumsq._2-Math.pow(sum._2,2));
var pearson = numerator/denominator;

println("\n\nPearson Correlation Coefficient : " + pearson);