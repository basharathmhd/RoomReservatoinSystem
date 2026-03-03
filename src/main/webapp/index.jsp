<!DOCTYPE html>
<html>
  <head>
    <title>Room Reservation System - Server Status</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }
      .container {
        text-align: center;
        background: white;
        padding: 50px;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      }
      .status {
        color: #4caf50;
        font-size: 24px;
        font-weight: bold;
        margin: 20px 0;
      }
      .indicator {
        width: 20px;
        height: 20px;
        background-color: #4caf50;
        border-radius: 50%;
        display: inline-block;
        margin-right: 10px;
        animation: pulse 1.5s infinite;
      }
      @keyframes pulse {
        0%,
        100% {
          opacity: 1;
        }
        50% {
          opacity: 0.5;
        }
      }
      h1 {
        color: #333;
        margin: 0;
      }
      p {
        color: #666;
        margin-top: 10px;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <h1>Room Reservation System</h1>
      <div class="status">
        <span class="indicator"></span>
        Server is Running
      </div>
      <p>Application is ready to accept requests</p>
    </div>
  </body>
</html>
