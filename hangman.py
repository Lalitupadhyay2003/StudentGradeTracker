import random

word_list = ["apple", "bread", "zebra", "house", "snake"]
chosen_word = random.choice(word_list)
display = ["_"] * len(chosen_word)
lives = 6
guessed_letters = []
print("Welcome to Hangman!")
print("Guess the word, one letter at a time.")
print("You have 6 lives. Good luck!\n")

while lives > 0 and "_" in display:
    print("Word: ", " ".join(display))
    guess = input("Guess a letter: ").lower()

    if guess in guessed_letters:
        print("You already guessed that letter. Try another one.\n")
        continue

    guessed_letters.append(guess)

    if guess in chosen_word:
        for index in range(len(chosen_word)):
            if chosen_word[index] == guess:
                display[index] = guess
        print("Good guess!\n")
    else:
        lives -= 1
        print(f"Wrong guess. You have {lives} lives left.\n")

if "_" not in display:
    print("Congratulations! You guessed the word:", chosen_word)
else:
    print("Game Over! The word was:", chosen_word)

