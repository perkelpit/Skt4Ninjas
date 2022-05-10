package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.company.skt.Skt;
import com.company.skt.lib.*;
import com.company.skt.model.Assets;
import com.company.skt.model.Local;
import com.company.skt.model.SessionData;
import com.company.skt.model.Settings;
import com.company.skt.view.*;

import java.io.IOException;

/**
 * {@code Menu} is the {@link StageScreen} for all those menus until the actual game session starts: <br>
 * Main menu, settings menu, (planned)profiles menu and lobby. <br>
 * It handles all the {@code clicks} and {@code events} happening within those UIs and corresponding
 * controller- and model-classes via {@link #click(String)} and {@link #event(String)}. */
public class Menu extends StageScreen implements HasSession {

    private Session session;
    private SessionData data;

    @Override
    public void initialize() {
        super.initialize();
        Assets.finishLoading();
        addStage(new MenuBackground("menuBackground", true));
        addStage(new MainMenuUI("mainMenuUI", true));
        addStage(new SettingsUI("settingsUI"));
        data = SessionData.get();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    public void click(String click) {
        String mainCase;
        String subCase;
        if (click.contains("#")) {
            mainCase = click.substring(0, click.indexOf("#"));
            subCase = click.substring(click.indexOf("#") + 1);
        } else {
            mainCase = click;
            subCase = "";
        }

        switch (mainCase) {
            // *** MAIN MENU-CLICKS ***
            case "HOST":
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                // TODO possibility to choose wether to start a new session or to load an saved one
                session = new HostSession();
                break;
            case "JOIN":
                DialogUI.newInputDialog(
                    findStage("mainMenuUI"), "IP", "localhost",
                    Local.get("dialog_title_ip"), Local.get("dialog_prompt_ip"),
                    null, null, findStage("mainMenuUI"), findStage("mainMenuUI"),
                        arg0 -> {
                            if (arg0 == null || arg0.isEmpty()) {
                                return false;
                            }
                            if (arg0.equals("localhost")) {
                                return true;
                            } else {
                                String[] parts = arg0.split("\\.");
                                if (parts.length != 4) {
                                    return false;
                                }
                                for (String part : parts) {
                                    int i = Integer.parseInt(part); //
                                    if (i < 0 || i > 255) {
                                        return false;
                                    }
                                }
                                return !arg0.endsWith(".");
                            }
                        },
                    null);
                break;
            case "ARCHIVE":
                DebugWindow.println("[MainMenu] archive clicked");
                DebugWindow.setUIFocus(DebugWindow.Focus.Archive);
                break;
            case "SETTINGS":
                DebugWindow.setUIFocus(DebugWindow.Focus.Settings);
                setStageActive("mainMenuUI", false);
                setStageActive("settingsUI", true);
                break;
            case "CREDITS":
                DebugWindow.println("[MainMenu] credits clicked");
                break;
            case "EXIT":
                UpdateStage mainMenuUI = findStage("mainMenuUI");
                DialogUI.newYesNoQuestion(
                        mainMenuUI, Local.get("dialog_question_quit"),
                        null, null, null, mainMenuUI, mainMenuUI,
                        () -> {
                            Skt.setStop(true);
                            Skt.getExecutor().shutdown();
                            DebugWindow.disposeDebugWindow();
                            Gdx.app.exit();
                        }, null);
                break;
            // *** SETTINGS MENU-CLICKS ***
            case "CHANGE_NAME":
                DebugWindow.println("[Settings] change name clicked");
                break;
            case "QUIT_SETTINGS":
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                setStageActive("mainMenuUI", true);
                setStageActive("settingsUI", false);
                break;
            case "QUIT_LOBBY":
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                Gdx.app.postRunnable(() -> {
                    DialogUI.newYesNoQuestion(
                            findStage("lobbyUI"),
                            (SessionData.isHost() ? Local.get("lb_q_end") : Local.get("lb_q_quit")),
                            null, null, null, findStage("mainMenuUI"), findStage("lobbyUI"),
                            () -> {
                                try {
                                    session.stopSession();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Gdx.app.postRunnable(() -> {
                                    Utils.getCurrentScreen().removeStage(findStage("lobbyUI"));
                                });
                            }, null);
                });
                break;
            case "GAME_SETTINGS_CLICKED":
                switch (subCase) {
                    case "CHANGE_NAME":
                        Settings.setProperty(Settings.APP, "player_name",
                                ((SettingsUI) findStage("settingsUI")).changeNameTextField.getText());
                        break;
                    case "LOST_FACTOR":
                        Settings.setProperty(Settings.GAME, "lost_factor",
                                ((SettingsUI) findStage("settingsUI")).lostFactorSelectBox.getSelected());
                        break;
                    case "RAMSCH":
                        Settings.setProperty(Settings.GAME,"ramsch",
                                String.valueOf(((SettingsUI) findStage("settingUI")).junkCheckbox.isChecked()));
                        break;
                    case "AMOUNT_GAMES":
                        Settings.setProperty(Settings.GAME, "amount_games",
                                ((SettingsUI) findStage("settingsUI")).amountGamesSelectBox.getSelected());
                        break;
                    case "TIME_LIMIT":
                        switch (((SettingsUI) findStage("settingsUI")).timeLimitSelectBox.getSelectedIndex()) {
                            case 0:
                                Settings.setProperty(Settings.GAME, "time_limit", "0");
                                break;
                            case 1:
                                Settings.setProperty(Settings.GAME, "time_limit", "30");
                                break;
                            case 2:
                                Settings.setProperty(Settings.GAME, "time_limit", "60");
                                break;
                            case 3:
                                Settings.setProperty(Settings.GAME, "time_limit", "120");
                                break;
                            case 4:
                                Settings.setProperty(Settings.GAME, "time_limit", "180");
                                break;
                            case 5:
                                Settings.setProperty(Settings.GAME, "time_limit", "300");
                                break;
                            case 6:
                                Settings.setProperty(Settings.GAME, "time_limit", "600");
                                break;
                        }
                }
                //Settings.acceptAltCfg(); // TODO acceptaltcfg + button
                break;
            case "LOBBY_SETTINGS_CLICKED":
                switch (subCase) {
                    case "LOST_FACTOR":
                        data.setCfgValue("lost_factor", ((LobbyUI) findStage("lobbyUI")).lostFactorSelectBox.getSelected());
                        break;
                    case "RAMSCH":
                        data.setCfgValue("ramsch", String.valueOf(((LobbyUI) findStage("lobbyUI")).junkCheckbox.isChecked()));
                        break;
                    case "AMOUNT_GAMES":
                        data.setCfgValue("amount_games", ((LobbyUI) findStage("lobbyUI")).amountGamesSelectBox.getSelected());
                        break;
                    case "TIME_LIMIT":
                        switch (((LobbyUI) findStage("lobbyUI")).timeLimitSelectBox.getSelectedIndex()) {
                            case 0:
                                data.setCfgValue("time_limit", "0");
                                break;
                            case 1:
                                data.setCfgValue("time_limit", "30");
                                break;
                            case 2:
                                data.setCfgValue("time_limit", "60");
                                break;
                            case 3:
                                data.setCfgValue("time_limit", "120");
                                break;
                            case 4:
                                data.setCfgValue("time_limit", "180");
                                break;
                            case 5:
                                data.setCfgValue("time_limit", "300");
                                break;
                            case 6:
                                data.setCfgValue("time_limit", "600");
                                break;
                        }
                }
                break;
            case "READY":
                if (SessionData.isHost()) {
                    // TODO switch to SummaryUI/PlayScrenn
                } else {
                    ((ClientSession) session).sendString("RDY_TGL");
                }
                break;
            case "KICK_PLAYER":
                switch (subCase) {
                    case "1":
                        // TODO remove player1
                        break;
                    case "2":
                        // TODO remove player2
                        break;
                }
                break;
            default:
                DebugWindow.println("buttonName " + click + " in " +
                        this.getClass().getSimpleName() + " not found");
        }
    }

    public void event(String event) {
        String mainCase;
        String subCase;
        if (event.contains("#")) {
            mainCase = event.substring(0, event.indexOf("#"));
            subCase = event.substring(event.indexOf("#") + 1);
        } else {
            mainCase = event;
            subCase = "";
        }
        switch (mainCase) {
            case "READY_FOR_LOBBY":
                DebugWindow.println("[Menu|Event] ready for lobby");
                if (!SessionData.isHost()) {
                    DialogUI.setTrigger(Trigger.SUCCESS);
                } else {
                    Gdx.app.postRunnable(() -> {
                        addStage(new LobbyUI("lobbyUI", true));
                        setStageActive("mainMenuUI", false);
                        ((LobbyUI) findStage("lobbyUI")).updateUI();
                    });
                }
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                break;
            case "READY_FOR_SUMMARY":
                DebugWindow.println("[Menu|Event] ready for summary");
                // TODO open SummaryUI
                break;
            case "SESSION_DATA_CHANGED":
                DebugWindow.println("[Menu|Event] session data changed");
                if (SessionData.isHost() && session != null) {
                    ((HostSession) session).sendStringToAll(SessionData.getDataStringForClient());
                }
                if (findStage("lobbyUI") != null) {
                    ((LobbyUI) findStage("lobbyUI")).updateUI();
                }
                if (Skt.isDebug()) {
                    DebugWindow.update();
                }
                break;
            case "LEAVE_LOBBY":
                DebugWindow.println("[Menu|Event] leave lobby");
                DebugWindow.setUIFocus(DebugWindow.Focus.Main);
                Gdx.app.postRunnable(() -> {
                    setStageActive("mainMenuUI", true);
                    setStageActive("lobbyUI", false);
                    removeStage("lobbyUI");
                });
                break;
            case "DIALOG_INPUT_READY":
                switch (subCase) {
                    case "IP":
                        session = new ClientSession(DialogUI.getInputString());
                        /*DialogUI.newTriggerMessage(findStage("mainMenuUI"), null,
                                Local.getString("dialog_msg_server_searching"),
                                null, null, findStage("mainMenuUI"),
                                findStage("mainMenuUI"), null, null, null, 5000);*/
                        break;
                    case "PLAYER_NAME":
                        // TODO
                        break;
                }
                break;
            case "CLIENT_SERVER_FOUND":
                //DialogUI.setTrigger(Trigger.SUCCESS);
                Gdx.app.postRunnable(() -> {
                    addStage(new LobbyUI("lobbyUI"));
                    ((LobbyUI) findStage("lobbyUI")).updateUI();
                    DialogUI.newTriggerMessage(
                            findStage("mainMenuUI"), null,
                            Local.get("dialog_msg_server_found_trying_to_join_lobby"),
                            null, null, findStage("lobbyUI"),
                            findStage("mainMenuUI"), null, () -> {
                                Gdx.app.postRunnable(() -> {
                                    try {
                                        session.stopSession();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    DialogUI.newOkMessage(
                                            findStage("mainMenuUI"), Local.get("rejected_title"),
                                            Local.get("rejected_message") + ".",
                                            null, findStage("mainMenuUI"), null);
                                });
                            }, null, 20000);
                });
                break;
            case "CLIENT_NO_SERVER_FOUND":
                //DialogUI.setTrigger(Trigger.FAIL);
                Gdx.app.postRunnable(() -> {
                    DialogUI.newOkMessage(
                        findStage("mainMenuUI"), Local.get("error") + "!",
                        Local.get("no_server_found") + ".", null, findStage("mainMenuUI"), null);
                });
                break;
            case "CONNECTION_REJECTED":
                DialogUI.setTrigger(Trigger.FAIL);
                break;
        }
    }

    public void setSessionToNull() {
        session = null;
    }
}
