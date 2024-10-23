# Group_4_SYSC3110_Project

#### Notes
- The project is not complete as yet
  - It will not run by running the JAR file. It must be extracted and run in IntelliJ
  - The following still need to be done
    - Enforcements of Scrabble rules still need to be implemented
    - Allowing words to be built off of each other also need to be implemented 
    - The scoring system also needs to be implemented as well
    - Win conditions for the game need to be implemented
    - Refactoring for clarity
    - Packaging to run in a JAR file

## How to play
- Upon running the game, a board will show and a prompt to enter the number and names of player's pops up
- The format of play is 
  - \[word (e.g. apple)] \[play coordinates (e.g. ff)] \[play direction (e.g. \[h | v])]  *any word starting with 'h' or 'v' would work as well
- The game decides who goes first based on the rules of scrabble (Closest to A goes first)
- The game runs until a winner is determined under the rules of scrabble.

---
#### Explanation for the use of data structures
- See data_structure_explanations.md
  - HashSet<String> validWords: ScrabbleGame
  - Square[][] board: ScrabbleBoard
  - HashMap<String, Integer> LETTER_FREQUENCY: TileBag
  - HashMap<String, Integer> LETTER_VALUE: TileBag
