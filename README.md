#The Problem
Pretty straightforward: grab some JSON from a URL, and generate a list where each element has icons and some text based on the JSON's content. 

#My Solution
I set up an activity with a Button (to start the process) and ListView (to display the content). 

On button press, the GET request is formed and sent off to another thread, which grabs the content from the provided URL, parses the JSON, and sends the parsed JSON off to another thread which will retrieve the images. 

The next thread grabs each image from the provided icon URLs and stores them in corresponding order to the events in the JSON. 

When all the images are retrieved, I feed the JSON and retrieved images into a custom adapter and populate the ListView in the activity.

#My prior experience
I haven't done any applications which interact with the internet on Android prior to this challenge, as the applications that I've worked on so far have been localized to the device itself. However, JSON and GET requests are all pretty familiar territory for me, so finding my bearings here wasn't too bad in terms of knowing roughly what I had to use and how everything had to come together.

#What I'd do differently
I'd use a library for the web interactions to keep the code cleaner and smaller; the AsyncTasks are a bit of a mess to just leave hanging around like that in the main source file. I think I could've done all of the web retrieval cleanly in one thread as well, but at the time it felt like it would be easier to read this way. There's no feedback after the button press to indicate any sort of progress, so from a UX perspective that's not acceptable. 
