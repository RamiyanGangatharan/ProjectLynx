# PROJECT LYNX
Stay sharp. Stay connected.

## Resources Used
Mostly me messing around, plus a YouTube tutorial that saved me like 6 hours of crying: https://youtu.be/hIc_9Wbn704

## What This Is

Project LYNX is basically me trying to make a scuffed Discord clone in Java. It’s a student project, not a real startup (yet).
The goal is just to learn networking, sockets, threading, and how not to segfault Java (which is impossible).
## Why I’m Making It
I wanted hands-on experience with building a tiny chat system from scratch instead of relying on libraries that do all the hard stuff for me. 
Discord is my main inspiration as I have literally used it every day since 2017.
Also, I thought “Project LYNX” sounded cool and now here we are.

## How It Works (Very Simply)
- The server runs on port 9999.
- When someone connects, the server spins up a thread for them.
- Everyone’s messages get broadcasted to everyone else.
- That’s… pretty much it right now. Very barebones chaos.
  Features (If You Can Call Them That)

## Basic chat messaging
- Nickname support
  - /nick command
- /quit command
- “Welcome to Project LYNX!” (peak UX)
- Crashes sometimes (feature not a bug)
  What I Built This With

## What I built this with
- Java
- Sockets
- BufferedReader / PrintWriter
- Threads and thread pools
- Pure willpower and caffeine

## Modules (In Human Words)
- Server: Accepts clients, throws them in a thread, relays messages.
- Client: Connects to server, sends and receives messages.
- ConnectionHandler: The middle guy that handles each user.
- InputHandler: Reads whatever you type and fires it at the server.

## How Data Flows
- You type --> client sends --> server gets --> server broadcasts --> everyone sees your message (including you).
- It’s like Discord but without literally everything that makes Discord good.

## Security (LMAO)

### **Do NOT use this over the internet unless you want to get owned instantly.**

Right now it’s:
- No authentication
- No encryption
- No real protections


## How To Run It

- Run the server first
- Run as many clients as you want
- Start yelling into the terminal
- This is all local for now.

## Roadmap (soft goals)
- Add channels
- Add DM support
- Add timestamps
- Add colors or username formatting
- Build an actual UI instead of terminal spam
- Maybe refactor the code so future me doesn’t scream

Licence: not sure at all, take if you want.