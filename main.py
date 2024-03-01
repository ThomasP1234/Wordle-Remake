####################################################################################################
#
# Author: Thomas Preston
#
# wordList.json is a sublist of https://github.com/dwyl/english-words/blob/master/words_dictionary.json
#
####################################################################################################

# Imports
import random
import json

# Constants
LETTERS = 'abcdefghijklmnopqrstuvwxyz'
LETTERS_LIST = list(LETTERS)
LIVES = 6
WORD_LIST = "wordList.json"

# Game Class
class WordleGame():
    def instructions(self):
        print("Wordle:")
        print("After entering your guess, symbols will appear underneath to indicate which letters are correct")
        print("For example:")
        print("apple")
        print("-^  ^")
        print("This means the 'a' is in the correct spot and the 'p' and 'e' are in the word but not in those spots")
        print("It is important to note that this shows there is only 1 'p' in the word")
        print("At any point during the game, you can type in 'info' or 'i' to get number of lives and already guessed letters")

    def loadWord(self):
        self.guessedLetters = [" "]*26

        file = open(WORD_LIST, "r")
        self.data = json.loads(file.read())
        file.close()

        self.word = random.choice(list(self.data.keys()))

    def getGuess(self):
        self.guess = ""
        while True:
            self.guess = input("Enter a 5 letter word ").lower()
            # Validate and sanitise input
            if self.guess == 'info' or self.guess == 'i':
                self.info()
                continue
            if len(self.guess) != 5:
                print("Needs to be only 5 letters")
                continue
            if not(self.guess in self.data):
                print("Not in word list")
                continue
            break

    def info(self):
        print(f"Lives: {self.lives}")
        print(LETTERS)
        print("".join(self.guessedLetters)) # Shows which letters are correct/misplaced/wrong
        print("")

    def checkGuess(self):
        if self.guess == self.word:
            self.correct = True
            return
        guessList = list(self.guess)
        wordList = list(self.word)
        puzzleList = [" "]*5
        counter = 0
        for id, letter in enumerate(guessList):
            if letter == wordList[id]:
                puzzleList[id] = wordList[id] = self.guessedLetters[LETTERS_LIST.index(letter)] = "-"
                counter += 1
                continue
            try:
                id2 = wordList.index(letter)
                puzzleList[id] = wordList[id2] = self.guessedLetters[LETTERS_LIST.index(letter)] = "^"
                counter += 1
                continue
            except ValueError:
                self.guessedLetters[LETTERS_LIST.index(letter)] = "x"
        if counter > 0:
            print(self.guess)
            print("".join(puzzleList))
        else:
            print("No letters were correct")
            
    def win(self):
        print("You guessed the word correctly - Well done")
    
    def loose(self):
        print(f"You loose - the correct word was {self.word}")

    def run(self):
        self.correct = False
        self.lives = LIVES
        self.instructions()
        self.loadWord()
        while self.correct == False and self.lives > 0:
            self.getGuess()
            self.checkGuess()
            self.lives -= 1
        if self.lives > 0:
            self.win()
        else:
            self.loose()

if __name__ == "__main__":
    game = WordleGame()
    game.run()