# Dolog

This is a log searcher which I created which eliminated the previous manual method of log
 searching, and replaced it with 4 clicks!
It uses Play Framework as the web stack, so it should work with Heroku, but I am having issues with that.

Backend is written in Java 8.
Front-end is written in HTML5, Jquery and CSS3.

## How to set-up

Download and install Activator: https://typesafe.com/activator
Play Framework uses Activator.

Once installed, navigate to the folder of this project and run
`activator run` in a shell such as cmd

Go to http://localhost:9000

## How to use

1. Upload a file
2. Scroll to the bottom
3. If you want to search, click on the General Regex Searcher, if the file is a Debenhams log file, select the Debenhams API Analyser .
4. Press Process
5. If you wish to not upload, you can go to the Servers tab, and select log files directly from the server. Saves you downloading the log file from the server, then uploading it. Handy.
6. Reap rewards

## To-do
- [ ] Stricter validation to make things tighter
- [ ] A new more optimised search algorithm
- [ ] Restrict HTTP requests per user
- [ ] Filter results function