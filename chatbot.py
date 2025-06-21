def get_response(user_input):
    user_input = user_input.lower().strip()

    if user_input in ["hello", "hi", "hey"]:
        return "Hi there! 👋"
    elif user_input == "how are you":
        return "I'm just a bunch of Python code, but I'm doing great! 😄"
    elif user_input == "bye":
        return "Goodbye! 👋 Have a nice day!"
    elif user_input == "what's your name":
        return "I'm a simple chatbot created by Lalit!"
    elif user_input == "help":
        return "You can say things like 'hello', 'how are you', 'bye', or 'what's your name'."
    else:
        return "I'm not sure how to respond to that. Try saying 'help' for options."


def main():
    print("Welcome to the Simple Chatbot!")
    print("Type 'bye' to exit the chat.\n")

    while True:
        user_input = input("You: ")

        if user_input.lower().strip() == "bye":
            print("Bot: " + get_response(user_input))
            break

        response = get_response(user_input)
        print("Bot:", response)

if __name__ == "__main__":
    main()
