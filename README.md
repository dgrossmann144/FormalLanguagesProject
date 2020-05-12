# FormalLanguagesProject
CMPT 440 Formal Languages and Computability Project

## Requirements
1. A Java 8 JDK and JRE (version 8.251 known to work)

## Setup
1. Clone the repository (https://github.com/dgrossmann144/FormalLanguagesProject.git)
2. Navigate to the repository directory in a terminal
3. Create a folder named `bin` from that directory
4. Execute the command `javac -d ./bin @sources.txt`

## Usage Information
1. From the directory the repository is installed in, use the Grepy utility using the command `java -cp ./bin Grepy [-d "dfa-fileName"] [-n "nfa-fileName"] "regex" "file.txt"`
2. Valid characters in the input file are the following: a-z, A-Z, and 0-9
3. Valid characters in the regex are the following: a-z, A-Z, 0-9, &, @, (, ), *, and |
4. `&` is used in place of the empty string symbol epsilon
5. `@` is used as the empty set symbol
6. Input file must be a .txt file