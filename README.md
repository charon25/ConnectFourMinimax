# Connect Four Minimax

This a project to teach myself the minimax algorithm, using Connect Four as an example.

## Usage

```shell
java -jar ConnectFourMinimax-1.0.jar
```

You will then be prompted with the game parameters :
- Width and height of the game board, default is 7x6 (both must be strictly positive)
- Number of pieces in a row to win, default is 4 (must be strictly positive)
- Number of players, default is 2, max is 6
- Each player type. You can enter the ID or the name of the player type (case-insensitive).

## Types

| ID | NAME   | Description                                                                         |
|----|--------|-------------------------------------------------------------------------------------|
| 0  | Human  | The player will get prompted each turn to enter the column where they want to play. |
| 1  | Random | Plays a random column among the available ones.                                     |

