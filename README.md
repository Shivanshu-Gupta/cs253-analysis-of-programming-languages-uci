# CS253: Analysis of Programming Languages Assignments

## Week 1
`cd Week1`

To run code: `scala Main.scala ../pride-and-prejudice.txt`
- This will print the top 25 most freqeuent words along with their frequencies.

## Week 2
`cd Week2`

To run exercise 5: `scala Five.scala ../pride-and-prejudice.txt`

To run exercise 6: `scala Six.scala ../pride-and-prejudice.txt`

To run exercise 7: `scala Seven.scala ../pride-and-prejudice.txt`

- All will print the top 25 most freqeuent words along with their frequencies.

## Week 3
`cd Week3`

To run exercise 8: `python Eight.py ../pride-and-prejudice.txt`

To run exercise 9: `scala Nine.scala ../pride-and-prejudice.txt`

To run exercise 10: `scala Ten.scala ../pride-and-prejudice.txt`

- All will print the top 25 most freqeuent words along with their frequencies.

## Week 4
`cd Week4`

To run exercise 12: `scala Twelve.scala ../pride-and-prejudice.txt`

To run exercise 13: `scala Thirteen.scala ../pride-and-prejudice.txt`

To run exercise 15: `scala Fifteen.scala ../pride-and-prejudice.txt`

- All will print the top 25 most freqeuent words along with their frequencies.
- Ex. 15 (Fifteen.scala) will also print #words containing z.

## Week 5
`cd Week5`

To run exercise 17: `java Seventeen.java ../pride-and-prejudice.txt`

To run exercise 20:  
```
cd Twenty

cd framework
javac -cp json-simple-1.1.jar *.java
jar -xf json-simple-1.1.jar
jar cfm framework.jar manifest.mf *.class org
rm -rf *.class META-INF org
cd ..

cd app1
javac -cp ../framework/framework.jar *.java
jar cf app1.jar *.class
rm *.class
cd ..

cd app2
javac -cp ../framework/framework.jar *.java
jar cf app2.jar *.class
rm *.class
cd ..

cd deploy
cp ../framework/framework.jar ../app1/app1.jar ../app2/app2.jar .
java -jar framework.jar ../../../pride-and-prejudice.txt
cd ..
```
- You can choose which plugin to run in `Twenty/deploy/config.json`
- [Fat JAR reference](https://dzone.com/articles/java-8-how-to-create-executable-fatjar-without-ide)

# Week 6
`cd Week6`

To run exercise 29: `scala TwentyNine.scala ../pride-and-prejudice.txt`

To run exercise 30: `scala Thirty.scala ../pride-and-prejudice.txt`

To run exercise 32: `scala ThirtyTwo.scala ../pride-and-prejudice.txt`

- All will print the top 25 most freqeuent words along with their frequencies.

# Week 7
`cd Week7`

To run exercise 27: `scala TwentySeven.scala ../pride-and-prejudice.txt`

To run exercise 28: `scala TwentyEight.scala ../pride-and-prejudice.txt`

# Week 8
`cd Week8`

To run exercise 3: `python Three.py ../pride-and-prejudice.txt`
