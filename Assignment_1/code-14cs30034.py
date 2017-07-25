from random import shuffle
import math

test=0

list1=[]
with open('data.txt','r') as f:
	list1=f.readlines()
sample=[]
j=0
for item in list1:
	it=item.strip()
	words=[]
	words=it.split("\t")
	l=[]
	l.append(words[1])
	l.append(words[2])
	l.append(words[3])
	l.append(words[4])
	sample.append(tuple(l))

t0_old=0
t1_old=0
t2_old=0
t3_old=0
t0=0
t1=0
t2=0
t3=0
#shuffle(sample)
j=0;
while(True):
	sum1=0
	k=0
	print j,sum1
	for item in sample:
		sof=t0_old + (t1_old * float(item[0])) + (t2_old * float(item[1])) + (t3_old * float(item[2]))
		der=float(item[3])- ((math.exp(sof))/(1 + math.exp(sof)))
		r=5
		t0 = t0_old + (der * r)
		t1 = t1_old + (der * r * float(item[0]))
		t2 = t2_old + (der * r * float(item[1]))
		t3 = t3_old + (der * r * float(item[2]))

		t0_old=t0
		t1_old=t1
		t2_old=t2
		t3_old=t3
		sum1+=(der*der)
		k+=1
		if test==1:
			if k==80:
				break
	j+=1
	if(sum1<0.005):
		break
print j,sum1
print t0," ",t0_old
print t1," ",t1_old
print t2," ",t2_old
print t3," ",t3_old

if test==1:
	j=80
	acc=0.0
	while(j<len(sample)):
		sof=t0_old + (t1_old * float(sample[j][0])) + (t2_old * float(sample[j][1])) + (t3_old * float(sample[j][2]))
		lr=math.exp(sof)/(1 + math.exp(sof))
		if lr <= 0.5 :
			if sample[j][3]=='0':
				acc+=1
		else:
			if sample[j][3]=='1':
				acc+=1
		j+=1
	print acc/20.0