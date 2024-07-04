const express = require('express');
const fs = require('fs');
const path = require('path');
const app = express();
const port = 3000;

// Middleware to parse JSON bodies
app.use(express.json());

// Endpoint to handle POST requests with a message
app.post('/message', (req, res) => {
    // Check if request body contains a 'message' property
    const { message } = req.body;
    if (!message) {
        return res.status(400).json({ error: 'Message is required' });
    }

    // Log the received message to a local file
    logMessage(message);

    // Send back a response
    res.json({ receivedMessage: message });
});

// Function to log message to a local file with timestamp
function logMessage(message) {
    const timestamp = new Date().toISOString();
    const logEntry = `${message} @ ${timestamp}\n`;
    const logFilePath = path.join(__dirname, 'server.log');

    // Append the log entry to the file
    fs.appendFile(logFilePath, logEntry, (err) => {
        if (err) {
            console.error('Error writing to log file:', err);
        } else {
            console.log('Message logged to file:', logEntry.trim());
        }
    });
}

// Start the Express server
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});

