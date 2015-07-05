package ca.josephroque.partners.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Date;

import ca.josephroque.partners.R;

/**
 * Created by Joseph Roque on 2015-06-18.
 *
 * Methods and constants related to identifying messages.
 */
public final class MessageUtil
{

    /** To identify output from this class in the Logcat. */
    @SuppressWarnings("unused")
    private static final String TAG = "MessageUtil";

    /** Parse object identifier for user's online status. */
    public static final String STATUS = "Status";
    /** Represents a boolean indicating user's online status. */
    public static final String ONLINE_STATUS = "status_online";
    /** Represents Parse object id for status object. */
    public static final String STATUS_OBJECT_ID = "status_object_id";
    /** Represents a boolean indicating if a thought has been sent since the app opened. */
    public static final String THOUGHT_SENT = "thought_sent";
    /** Represents a boolean indicating if a status update has been sent since the app opened. */
    public static final String STATUS_SENT = "status_sent";

    /** A login message. */
    public static final String LOGIN_MESSAGE = "~LOGIN";
    /** A logout message. */
    public static final String LOGOUT_MESSAGE = "~LOGOUT";
    /** An intent action for when a message is received. */
    public static final String ACTION_MESSAGE_RECEIVED = "action_message_received";

    /** Maximum length of messages. */
    public static final int MAX_MESSAGE_LENGTH = 140;
    /** Number of characters at the start of a message which indicate its type. */
    public static final int MESSAGE_TYPE_RESERVED_LENGTH = 4;
    /** Regular expression for a valid message. */
    public static final String REGEX_VALID_MESSAGE = "^[A-Za-z0-9 \\r\\n@£$¥èéùìòÇØøÅå\u0394_\u03A6\u0393\u039B\u03A9\u03A0\u03A8\u03A3\u0398\u039EÆæßÉ!\"#$%&amp;'()*+,\\-./:;&lt;=&gt;?¡ÄÖÑÜ§¿äöñüà^{}\\\\\\[~\\]|\u20AC]*$";


    /** If found at the start of a message, indicates the message describes an error. */
    public static final String MESSAGE_TYPE_ERROR = "ERR";
    /** If found at the start of a message, indicates the message can be sent to a partner. */
    public static final String MESSAGE_TYPE_VALID = "MSG";

    /**
     * Default private constructor.
     */
    private MessageUtil()
    {
        // does nothing
    }

    /**
     * Gets the current date and time, formatted 'yyyy-MM-dd HH:mm:ss'.
     *
     * @return current date and time
     */
    public static String getCurrentDateAndTime()
    {
        return Long.toString(new Date().getTime());
    }

    /**
     * Checks to see if the string is a valid message to send as a thought, and if not a string of
     * the format 'ERR:X' is returned, where X is the id of a string.
     *
     * @param message message to check
     * @return if {@code message} is valid, a String formatted 'MSG:message'. Otherwise, returns a
     * String formatted 'ERR:X'
     */
    public static String getValidMessage(String message)
    {
        if (LOGIN_MESSAGE.equals(message) || LOGOUT_MESSAGE.equals(message))
            return MESSAGE_TYPE_VALID + ":" + message;

        if (message == null || message.length() == 0)
            return MESSAGE_TYPE_ERROR + ":" + R.string.text_no_message + ":" + message;

        message = message.trim();
        if (message.length() > MAX_MESSAGE_LENGTH)
            return MESSAGE_TYPE_ERROR + ":" + R.string.text_message_too_long
                    + ":" + message;
        else if (!message.matches(REGEX_VALID_MESSAGE))
            return MESSAGE_TYPE_ERROR + ":" + R.string.text_message_invalid_characters
                    + ":" + message;
        else
            return MESSAGE_TYPE_VALID + ":" + message.trim();
    }

    /**
     * Displays a snackbar with the error message.
     *
     * @param rootView view for snackbar
     * @param error error message
     */
    public static void handleError(View rootView, String error)
    {
        final int errorId;
        final String message;
        try
        {
            final String[] errorComponents = error.split(":", 3);
            if (!errorComponents[0].equals(MESSAGE_TYPE_ERROR))
                throw new IllegalArgumentException();
            errorId = Integer.parseInt(errorComponents[1]);
            message = errorComponents[2];
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Error message must follow format 'ERR:X:message' "
                    + "where X is an integer and message is the message that caused the error");
        }

        if (errorId == R.string.text_message_too_long)
        {
            ErrorUtil.displayErrorSnackbar(rootView, "Message length over limit: "
                    + message.length() + "/" + MessageUtil.MAX_MESSAGE_LENGTH);
        }
        else
        {
            ErrorUtil.displayErrorSnackbar(rootView,
                    rootView.getContext().getResources().getString(errorId));
        }
    }

    /**
     * Sets whether a thought was sent since the app was opened. Should be set to false each time
     * the app opens.
     *
     * @param context to get shared preferences
     * @param sent new value for {@code THOUGHT_SENT} in {@link android.content.SharedPreferences}
     */
    public static void setThoughtSent(Context context, boolean sent)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(THOUGHT_SENT, sent)
                .apply();
    }

    /**
     * Returns true if a thought has been sent since the app was opened, false otherwise.
     *
     * @param context to get shared preferences
     * @return true if a thought was sent, false otherwise
     */
    public static boolean wasThoughtSent(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(THOUGHT_SENT, false);
    }

    /**
     * Sets whether a status update was sent since the app was opened. Should be set to false each
     * time the app opens.
     *
     * @param context to get shared preferences
     * @param sent new value for {@code STATUS_SENT} in {@link android.content.SharedPreferences}
     */
    public static void setStatusSent(Context context, boolean sent)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(STATUS_SENT, sent)
                .apply();
    }

    /**
     * Returns true if a status update has been sent since the app was opened, false otherwise.
     *
     * @param context to get shared preferences
     * @return true if a status update was sent, false otherwise
     */
    public static boolean wasStatusSent(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(STATUS_SENT, false);
    }
}
