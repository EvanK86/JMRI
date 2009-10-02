// AbstractAudioFactory.java

package jmri.jmrit.audio;

import java.util.LinkedList;
import java.util.List;
import jmri.Audio;

/**
 * Abstract implementation of the AudioFactory class.
 * <p>
 * All code shared amongst the concrete AudioFactory classes is defined here.
 * <hr>
 * This file is part of JMRI.
 * <p>
 * JMRI is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <p>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 * <p>
 *
 * @author Matthew Harris  copyright (c) 2009
 * @version $Revision: 1.4 $
 */
public abstract class AbstractAudioFactory implements AudioFactory {

    /**
     * List of queued audio commands to process
     */
    private static List<AudioCommand> commandQueue = null;

    /**
     * Boolean used to determine if this AudioFactory has been initialised
     */
    private static boolean _initialised = false;

    /**
     * Boolean used to determine if this AudioFactory should attenuate sources
     * based on their distance from the Listener
     */
    private static boolean _distanceAttenuated = true;

    /**
     * Reference to the seperate thread used to process all AudioCommands
     */
    private static AbstractAudioThread audioCommandThread = null;

    public boolean init() {
        if (_initialised) {
            log.debug("Already initialised");
            return true;
        }

        // Create the command queue
        commandQueue = new LinkedList<AudioCommand>();

        // Create and start the command thread
        audioCommandThread = new AudioCommandThread(this);
        audioCommandThread.start();

        _initialised = true;
        return true;
    }

    public void cleanup() {

        boolean dieException = false;

        // End the command thread
        try {
            audioCommandThread.die();       // send the die signal to the thread
            audioCommandThread.interrupt(); // interrupt the thread to process die signal
        } catch (Exception e) {
            dieException = true;
        }

        if (!dieException) {
            // wait for up to 5 seconds for thread to end
            for (int i=0; i<50; i++) {
                if (!audioCommandThread.alive()) {
                    break;
                }
                AbstractAudioThread.snooze(100);
            }
        }
    }

    public synchronized boolean AudioCommandQueue(AudioCommand queueAudioCommand) {
        if (queueAudioCommand == null) {
            log.debug("Processing command queue");
            // Process command queue
            AudioCommand audioCommand;
            while (commandQueue != null && commandQueue.size() > 0) {
                audioCommand = commandQueue.remove(0);
                if (audioCommand != null) {
                    if (log.isDebugEnabled())
                        log.debug("Process command: " + audioCommand.toString()
                                + " (" + commandQueue.size() + " remaining)");
                    Audio audio = audioCommand.getAudio();

                    // Process AudioSource commands
                    if (audio instanceof AudioSource) {
                        AbstractAudioSource audioSource = (AbstractAudioSource) audio;
                        switch (audioCommand.getCommand()) {
                            case Audio.CMD_BIND_BUFFER:
                                audioSource.setBound(audioSource.bindAudioBuffer(audioSource.getAssignedBuffer()));
                                break;
                            case Audio.CMD_PLAY:
                                audioSource.doPlay();
                                break;
                            case Audio.CMD_STOP:
                                audioSource.doStop();
                                break;
                            case Audio.CMD_PLAY_TOGGLE:
                                audioSource.doTogglePlay();
                                break;
                            case Audio.CMD_PAUSE:
                                audioSource.doPause();
                                break;
                            case Audio.CMD_RESUME:
                                audioSource.doResume();
                                break;
                            case Audio.CMD_PAUSE_TOGGLE:
                                audioSource.doTogglePause();
                                break;
                            case Audio.CMD_REWIND:
                                audioSource.doRewind();
                                break;
                            case Audio.CMD_FADE_IN:
                                audioSource.doFadeIn();
                                break;
                            case Audio.CMD_FADE_OUT:
                                audioSource.doFadeOut();
                                break;
                            case Audio.CMD_RESET_POSITION:
                                audioSource.doResetCurrentPosition();
                                break;
                            default:
                                log.warn("Command " + audioCommand.toString()
                                        + " not suitable for AudioSource (" + audioSource.getSystemName() + ")");
                        }
                    }

                    // Process AudioBuffer commands
                    else if (audio instanceof AudioBuffer) {
                        AbstractAudioBuffer audioBuffer = (AbstractAudioBuffer) audio;
                        switch (audioCommand.getCommand()) {
                            case Audio.CMD_LOAD_SOUND:
                                audioBuffer.loadBuffer();
                                break;
                            default:
                                log.warn("Command " + audioCommand.toString()
                                        + " not suitable for AudioBuffer (" + audioBuffer.getSystemName() + ")");
                        }
                    }

                    // Process AudioListener commands
                    else if (audio instanceof AudioListener) {
                        AbstractAudioListener audioListener = (AbstractAudioListener) audio;
                        switch (audioCommand.getCommand()) {
                            case Audio.CMD_RESET_POSITION:
                                audioListener.doResetCurrentPosition();
                                break;
                            default:
                                log.warn("Command " + audioCommand.toString()
                                        + " not suitable for AudioListener (" + audioListener.getSystemName() + ")");
                        }
                    }
                }
            }
            return (commandQueue != null && commandQueue.size()>0);
        } else {
            if (commandQueue == null) {
                log.warn("Audio commandQueue not initialised");
                return false;
            }
            commandQueue.add(queueAudioCommand);
            if (log.isDebugEnabled()) log.debug("New audio command: " + queueAudioCommand.toString());
            return true;
        }
    }

    public Thread getCommandThread() {
        return audioCommandThread;
    }

    public void setDistanceAttenuated(boolean attenuated) {
        _distanceAttenuated = attenuated;
    }

    public boolean isDistanceAttenuated() {
        return _distanceAttenuated;
    }

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractAudioFactory.class.getName());

}

/* @(#)AbstractAudioFactory.java */