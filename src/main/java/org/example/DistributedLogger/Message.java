package org.example.DistributedLogger;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * [12:18 PM, 9/19/2025] Ankur OC: You have to design and implement a Vehicle Rental Service which allows users to rent a given type of Vehicle for a given time slot.
 *
 * Requirements:
 * This Vehicle Rental Service will be operational only in one city (Delhi) which will have multiple branches (eg. Vasant Vihar Branch, CP branch, etc).
 * Each branch can have three different types of vehicles (VehicleType): Sedan, Hatchback, SUV. There could be any number of vehicles of each type in a branch.
 * The rental price per hour should be defined at “per branch per vehicle type” level and NOT at an individual vehicle level. (eg. Sedan in CP branch = 150 rs/hr, Sedan in Vasant Vihar = 100 rs/hr, Hatchback in CP = 80rs/hr and so on)
 * A user can request to rent a particular vehicle type for a given time slot. You wi…
 * [12:20 PM, 9/19/2025] Ankur OC: You need to design and implement a logger library that applications can use to log messages to a sink.
 *
 * Message:
 * A message has the following attributes:
 * Content: A string representing the actual log message.
 * Level: The severity level of the message.
 * Sink: The destination (like file, database, console, etc.) where the message is logged.
 * Namespace: Identifies the part of the application that sent the message.
 * Sink:
 * A sink is the destination for logging messages.
 * A sink is:
 * Tied to one or more message levels (e.g., ERROR, DEBUG).
 * Shared: Multiple message levels can use the same sink.
 * Logger Library Requirements:
 * Setup:
 * Requires configuration during sink setup.
 * Accepts messages from clients (applications).
 * Routes messages to the appropriate sink based on message levels.
 * Supports the following message levels in order of priority: FATAL, ERROR, WARN, INFO, DEBUG.
 * Allows configuration to log messages above a certain level:
 * Example: If the logging level is configured as INFO, only WARN, INFO, and DEBUG messages will be logged.
 * Enriches messages with additional information (like timestamps) before sending to the sink.
 * Sending Messages:
 * Message level determines the sink:
 * You don't need to specify the sink when sending a message; it's tied to the level.
 * Input required:
 * Message content.
 * Level.
 * Namespace.
 * Logger Configuration:
 * Purpose:
 * Defines all the details required to use the logger library.
 * Allows configuration of one or more sinks for an application.
 * Associates each message level with a specific sink.
 * Implementation:
 * Can be considered as key-value pairs for configuration.
 * Example Configuration Details:
 * Time format (e.g., ISO 8601).
 * Logging level (e.g., INFO, WARN).
 * Sink type (e.g., file, console, database).
 * Details required for the sink (e.g., file location).
 * Example:
 *
 * Configuration Example:
 * - Time Format: YYYY-MM-DD HH:mm:ss
 * - Logging Level: INFO
 * - Sink Type: File
 * - File Location: /var/logs/application.log
 * Key Functionalities:
 *
 * Sink Setup:
 * Associate sinks with message levels during configuration.
 * Message Routing:
 * Route messages based on their level to the appropriate sink.
 * Log Enrichment:
 * Add metadata (e.g., timestamps, namespace) to messages automatically.
 * Priority-Based Filtering:
 * Log only messages above the configured logging level.
 */
@Getter
public class Message {
    private final String content;
    private final LogLevel level;
    private LocalDateTime time;
    private final String namespace;
    private final Sink sink;

    public Message(String content, LogLevel level, String namespace, Sink sink) {
        this.content = content;
        this.level = level;
        this.namespace = namespace;
        this.sink = sink;
    }
    public void setTimeStamp(LocalDateTime time) {
        if(this.time == null) {
            this.time = time;
        }
    }
}
