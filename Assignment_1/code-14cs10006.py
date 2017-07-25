from math import exp,log

x=[]		#for storing the input data features
y=[]		#for storing the labels of each sample
with open("data.txt","r") as infile :	#reading the input file
	for line in infile :
		words=line.split()
		x1=[float(temp) for temp in words[1:4]]		#seperating and storing the features in x
		y1=int(words[4])							#seperating and storing the labels in y
		x.append(x1)
		y.append(y1)

theta=[0,0,0,0]										#initial parameter values
iterations=0				
sum_error=0											#sum of errors initialised to 0
for i in range(100000) : 							#convergence criteria specified in the end
	print "Iteration No : ",i 
	print "Parameter values : ",theta[0],theta[1],theta[2],theta[3]				#the updated parameter values after every iteration
	count=0
	prev_error=sum_error							#the error sum of previous iteration is updated here
	sum_error=0
	for j,sample in enumerate(x) :					#for each sample
		SOF=theta[0]+theta[1]*sample[0]+theta[2]*sample[1]+theta[3]*sample[2]	#the SOF function using the parameters to predict the labels
		LR=exp(SOF)/(1+exp(SOF))					#LR funtion to be used later to update parameters
		error=LR-y[j]								#error for jth sample defined here
		sum_error+=error**2							#to make the error positive, sum is calculated for all the samples
		if (LR > 0.5 and y[j]==0) or (LR<0.5 and y[j]==1) : count+=1			#count=training error = no. of false predictions
		for k,item in enumerate(theta) :			#for every parameter
			if k==0 : theta[k]=theta[k]+5*(y[j]-LR)	#updating theta using the update and derivative rule	
			else : 	theta[k]=theta[k]+5*sample[k-1]*(y[j]-LR)               	#the learning rate is chosen as constant = 5 after many experiments for suitable value to minimise no. of iterations and making sure the cost function converges
	print count,sum_error							#for debugging
	if sum_error < 0.005 : break					#convergence criteria, break the loop when sum of error squares is less than 0.01 

with open("output-14cs10006","w") as outfile :		#writing back to the output file
	outfile.write("The number of iterations : "+str(i)+"\nTraining error (number of false predictions on the training data) : "+str(count)+"\nSum of errors : "+str(sum_error)+"\nParameter values : theta0 = "+str(theta[0])+"\ttheta1 = "+str(theta[1])+"\ttheta2 = "+str(theta[2])+"\ttheta3 = "+str(theta[3]))