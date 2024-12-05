# Group_4_SYSC3110_Project

#### Notes
- The project is complete

## How to play
- GUI version of the game
  - Upon running the game, option panels will pop up and prompt you to input required information to continue with the game
    - "How many players are there?"
    - "Player x, what is your name?"
  - To place letters on the board
    - First, click the tile you want to place on the board from your tile rack
    - Second, click the square on the board you want to place it on
  - To play a word after placing the tiles on the board, press the play button on the bottom right of your screen
  - To skip a turn, press the pass button on the bottom right of your screen
  - To clear the board of any recently placed tiles that weren't played, press the clear button on the bottom right of your screen
  - To CONNECT a word to an existing word on the board (DISCLAIMER: This is a little buggy so expect some bugs)
    - This only works if the word you want to connect starts with the first letter of a word that is already on the board
      - Example: The word "IN"  is on the board. The only words that can be connected are words that start with "I" or "N" like "IT" or "NO". It aslo cannot be in the same direction the the word "IN" is written in.
    - First, click on the letter that you want to start the connected word with. This will give you a message that the letter has been added to your played words. To undo press the clear button.
    - DISCLAIMER: If you press the tile more than once, you MUST click the clear button, otherwise it will add that letter multiple times to your played word.
    - Second, fill in the rest of the word then press play.

- Handling Serialization
  - In the GUI Version, you can save the state of the game with the "Save" JMenuitem. Once selected, you can give the serialized file a name, click "OK" and it will be saved to your file directory.
  - To Load the game, you can click on the "Load" JMenuitem. Once selected, type in the name you saved your file as and click "OK".

- How to use undo/redo feature.
  - There will be a panel where you can see the undo and redo. Click it and a dropdown will appear.
  - Click undo or (ctrl + z) to undo
  - Click redo or (Ctrl + y) to redo
  - Make sure there are moves to be undone / redone


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


## Bonus Section
  - **Selected Feature: Winner Celebration**
    - When the number of tiles left in the bag is equal to 0, the game automatically disables all buttons and a GUI displaying a celebratory message for the winner pops up.
    - Also, you can end the game prematurely by selecting the "Quit" menu. The game will end and the highest scoring player will be celebrated.
  - **Contributors**
    - Abdul Babatunde
    - Clinton Odinamadu
    - Marvel Adotse-Ogah
---
#### Explanation for the use of data structures
- See data_structure_explanations.md
  - HashSet<String> validWords: ScrabbleGame
  - Square[][] board: ScrabbleBoard
  - HashMap<String, Integer> LETTER_FREQUENCY: TileBag
  - HashMap<String, Integer> LETTER_VALUE: TileBag
