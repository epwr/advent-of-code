# Advent of Code - 2020

## About

In an effort to teach myself Clojure, I am working through the Advent of Code 2020 examples. There are 25 days in the 'Advent Calendar', and each 'day' gives two problems. You need to complete the first problem to access the second one meaning the first part of the solution was created before I knew how (or if) it could be used in the second part. 

I am trying to write each solution in a fairly functional style which means (to me):

- As few non-pure functions as possible.
- Defering side effects until the last possible responsible moment.
- Concentrating side effects (so its easy to see them all at once).

Note: you need to create an account on Advent of Code to see the problems, so I tried to give a brief description of the problem in the code.

## What I Noticed

The first 6 days were mostly getting back up to speed in LISP & functional programming, and learning how Clojure differs from Racket/Common LISP. Using map, filter, reduce, every? instead of for-loops, getting used to minimizing the amount of state variables, writing pure functions, etc. One thing that did stand out was the joy of the hash-map, vector, and set datastructures over the usual focus of LISPs: lists. 

By day 7 I wanted to start exploring the clojure.spec feature (which adds a wonderfully flexible type verification system to a dyamically typed language) and Clojure's concurrency primitives. While clojure.spec is overkill for the kind of basic exploratory programming used to solve problems of this type, I thought it would be worth tackling that first.

## Links

You can find the Advent of Code problems at https://adventofcode.com/2020

## Notes

This project was developed using Clojure 1.10.1

