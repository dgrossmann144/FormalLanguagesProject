
# FormalLanguagesProject
CMPT 440 Formal Languages and Computability Project

## Requirements
1. A Java 8 JDK and JRE (version 8.251 known to work)

## Setup
1. Clone the repository (https://github.com/dgrossmann144/FormalLanguagesProject.git)
2. Navigate to the repository directory in a terminal
3. Create a folder named `bin` in that directory
4. Execute the command `javac -d ./bin @sources.txt`

## Usage Information
1. From the directory the repository is installed in, use the Grepy utility using the command `java -cp ./bin Grepy [-d "dfa-fileName"] [-n "nfa-fileName"] "regex" "file.txt"`
2. Valid characters in the input file are the following: a-z, A-Z, and 0-9
3. Valid characters in the regex are the following: a-z, A-Z, 0-9, &, @, (, ), *, and |
4. `&` is used in place of the empty string symbol epsilon
5. `@` is used as the empty set symbol
6. Input file must be a .txt file

## Examples
1. `java -cp ./bin Grepy "(a|b)*a" "exampleInputFiles/example1.txt"` outputs: aaaabbbba, aaaabbbabababa, a, abba, ba
2. `java -cp ./bin Grepy "@" "exampleInputFiles/example2.txt"` outputs: 
3. `java -cp ./bin Grepy "(0|1)(10)*0(0|1)" "exampleInputFiles/example3.txt"` outputs: 1101001, 000, 001, 101
4. `java -cp ./bin Grepy "(k|b|&)(ing)*" "exampleInputFiles/example4.txt"` outputs: king, bing, ing, kinging, binging, inginging
