# Advent of Code - 2020

## About

In an effort to teach myself Clojure, I am working through the Advent of Code 2020 examples. There are 25 days in the 'Advent Calendar', and each 'day' gives two problems. You need to complete the first problem to access the second one meaning the first part of the solution was created before I knew how (or if) it could be used in the second part. 

I am trying to write each solution in a fairly functional style which means (to me):

- As few non-pure functions as possible.
- Defering side effects until the last possible responsible moment.
- Concentrating side effects (so its easy to see them all at once).

Note: you need to create an account on Advent of Code to see the problems, so I tried to give a brief description of the problem in the code.

## What I Noticed

### Language Design

The first few days were about getting back up to speed using a LISP (it's been a bit since I've touched Racket), and figuring out some Clojure idioms. The main thing that stood out to me as a great feature of the language is that instead of thinking of all data collections as a list (which is what most LISPs do - and where they get the name LISP), Clojure treats data collections as a sequence. Sequences, in Clojure, are not a data type, but instead an abstraction. Instead of a list in a LISP (eg. Racket) being a collection of cons cells that are accessed via (car) and (cdr), sequences can be built up of anything, and can be accessed through (first) and (rest) This allows CLojure to have lists, hash and sorted maps, hash and sorted sets, and vectors, all be a basic data set, while maintaining the ease of code reuse. 

Rich Hickey - the creator of Clojure - is the first modern language designer I have seen who is a real designer. He is able to build something that is both so simple but with so much functionality that it's beautiful. Maybe I have a type, but I the only language designer that I would put in the same space as Hickey is McCarthy (the original designer of LISP).

### Elegance

How would I describe elegance?	

### Pattern Matching / Deconstruction

See pg. 206 of notebook.

### Concurrency


### Tail Recursion

Having spent a bit of time working with Racket and Standard ML, I'm used to having tail recursion be a big win for performance in functional programming languages. Unfortunately, the JVM doesn't support tail recursion. Instead, Clojure has a recur function which supposedly provides most of the performance improvements of tail recursion.

But to see how well it works, I used it in the solution to Day 8's Part 2 and found that the two solutions (one using recur and one using normal recursion) were almost identical in terms of time (although whichever one is run first is slower by a factor of 2). It makes sense that recur's main benefit is in memory usage because it does not grow the call stack.

### clojure.spec



## Links

You can find the Advent of Code problems at https://adventofcode.com/2020

## Misc Notes

This project was developed using Clojure 1.10.1

