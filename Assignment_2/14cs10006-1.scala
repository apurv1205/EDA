/* this code finds the mean feature vector for each label from the file mean.txt */
/* which contains 4 columns, 1st is the class label(Int) and rest are feature vectors of normalised floats */

/*this line is for loading data from disk and storing in the RDD (lines). Note that textFile() reads one line at a time */
val lines = sc.textFile("mean.txt")

/* this line converts the lines into sequence of words. The expression \\s+ inside split breaks the lines into words delimited by one or more space */
/* Here we use map to create RDD of the following type :
	(x,y) where x is the class label and y is the feature vector in list form containing three floating point numbers
	for example : (1,List(0.053700, -0.088746, 0.228501))
 */
val vector = lines.map{ line => (line.split("\\s+")(0).toInt, List( line.split("\\s+")(1).toFloat, line.split("\\s+")(2).toFloat, line.split("\\s+")(3).toFloat ) )}

/* This is for counting the number of features for each class, the RDD format is (class,1) */
val count1 = lines.map{ line => (line.split("\\s+")(0).toInt,1)}
/* We reduce the above RDD by collecting the tuples with same key values and adding their values to give counts for each label */
/* It contains a list of 5 tuples in the form of (class,count). We use coalesce to ensure the number of partitions =1 for sorting by key operation  */
val count = count1.reduceByKey{(x,y) => x+y}.coalesce(1).sortByKey(true)
/* We conver the above RDD into a Map for further use */
val countMap = count.collectAsMap

/* We now actually sum the feature vectors for each class by reducing it basd on key (class label) and then summing up the items in the lists */
/* We store it in sorted order after making the partition number = 1 */
val sum = vector.reduceByKey{(x, y) => List(x(0) + y(0),x(1) + y(1),x(2) + y(2))}.coalesce(1).sortByKey(true)

/* Here we calculate the meanVectors for each class using the countMap and the sum of features RDD which is already sorted*/
val meanVectors = sum.map{ x => ( x._1, List(x._2(0)/countMap(x._1),x._2(1)/countMap(x._1),x._2(2)/countMap(x._1)) ) }

println("\n\n***The count of vectors for each class are as follows :\n")
count.foreach(println)

println("\n\n***The sum of feature vectors for each class are as follows :\n")
/* finally save the result into a file output.txt */
for (item <- sum) {
	println(item._1+"\t:\t"+item._2(0)+"\t\t"+item._2(1)+"\t\t"+item._2(2))
}

println("\n\n***The mean feature vectors for each class are as follows (also can be saved in the file output_1.txt) :\n")
/* finally save the result into a file output.txt */
for (item <- meanVectors) {
	println(item._1+"\t:\t"+item._2(0)+"\t\t"+item._2(1)+"\t\t"+item._2(2))
}

/* finally save the result into a file output_1.txt */
/* uncomment the following code to output it to a file */
//meanVectors.saveAsTextFile("output_1.txt")