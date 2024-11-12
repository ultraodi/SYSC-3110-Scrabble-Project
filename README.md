# Group_4_SYSC3110_Project

#### Notes
- The project is incomplete
  - It will not run by running the JAR file. It must be extracted and run in IntellliJ
  - All words in the provided database are accounted for.
  - The initial designs for the GUI have been created.
  - Completed since the last Milestone
    - Enforcements of scrabble rules are now implemented.
    - Words can be built off each other as in a scrabble game.
    - A basic scoring system was implemented.
  - The following still need to be done
    - GUI that is consistent with the model.
    - AI players
    - Unit tests for the following classes:
      - Square
      - Play
      - Player
      - ScrabbleBoard
      - ScrabbleGame
      - Tile
      - TileBag
    - Unit tests for the different rules of scrabble
    - Unit tests for GUI elements
    - Win conditions for the game need to be implemented
    - Refactoring for clarity
    - Packaging to run in a JAR file

## How to play
- Text version of the game
  - Change TEXT_VERSION to true
  - Change DEBUG to false
  - Upon running the game, a board will show and a prompt to enter the number and names of player's pops up
  - The text version has a play format and commands to change the state of the game
    - The format of play is 
      - \[word (e.g. apple)] \[play coordinates (e.g. ff)] \[play direction (e.g. \[h | v])]  *any word starting with 'h' or 'v' would work as well
      - Example
        - apple 88 h
    - The possible commands are
      - '-debug': Enable debug messages
      - '-board': Print out board
      - '-noDebug': Disables debug messages
      - '-quit': Ends the game
      - '-skip': Skips the current player's turn
        - The commands are case-sensitive.
        - Example
          - -debug (This will enable the debug state of the game and error message will be printed out)
  - The game decides who goes first based on the rules of scrabble (Closest to A goes first)
  - The game runs until a winner is determined under the rules of scrabble.
- GUI version of the game
  - Change TEXT_VERSION to false

---
#### Explanation for the use of data structures
- See data_structure_explanations.md
  - HashSet<String> validWords: ScrabbleGame
  - Square[][] board: ScrabbleBoard
  - HashMap<String, Integer> LETTER_FREQUENCY: TileBag
  - HashMap<String, Integer> LETTER_VALUE: TileBag
