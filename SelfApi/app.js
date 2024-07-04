const express = require('express');
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

    // Log and send back the received message
    console.log('Received message:', message);
    res.json({ receivedMessage: message });
});

// Start the Express server
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});

// curl -X POST -H "Content-Type: application/json" -d '{"message": "Hello, Express!"}' http://localhost:3000/message
