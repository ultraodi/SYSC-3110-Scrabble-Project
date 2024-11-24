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
- GUI version of the game
  - Upon running the game, option panels will pop up and prompt you to input required information to continue with the game
    - "Would you like to play with AI yes or no:"
    - "How many players are there?"
    - "Player x, what is your name?"
  - To place letters on the board
    - First, click the tile you want to place on the board from your tile rack
    - Second, click the square on the board you want to place it on
  - To play a word after placing the tiles on the board, press the play button on the bottom right of your screen
  - To skip a turn, press the pass button on the bottom right of your screen
  - To clear the board of any recently placed tiles that weren't played, press the clear button on the bottom right of your screen
  - To connect a word to an existing word on the board multiple scenarios
      - **Scenario 1:** There is a word on the board that spells "RAVEN" and you want to add an "S" to the end and spell "RAVENS"
      - Click the letters in order of how to spell the word, this is to add it to your played tiles. 
      - Example: Click the letter "R" then "A" then "V" then "E" then "N". After this add your "S" tile to the board then click play.
      - **Scenario 2:** There is a word on the board that contains a letter needed to play a word. "RAVEN" and you have the letters "F, I, D" and you want to spell "FIND"
      - Place "F" two tiles before the "N", place "I" one tile before the "N", click the "N" tile on the board to add to your played tiles, place the "D" one tile after the "N" tile. After completing this press play.
  - To play against the AI of the game. Type "yes" to play against when prompted. Play as you would normally. After pressing play, the AI will make its play and the game will continue as normal.
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
#### Known Issues
- Triple Word and Double Word squares do not calculate the score correctly
- Tile colors don't revert back when the tiles are removed from the board

---
#### Explanation for the use of data structures
- See data_structure_explanations.md
  - HashSet<String> validWords: ScrabbleGame
  - Square[][] board: ScrabbleBoard
  - HashMap<String, Integer> LETTER_FREQUENCY: TileBag
  - HashMap<String, Integer> LETTER_VALUE: TileBag
