package com.company.skt.controller;

import com.badlogic.gdx.Gdx;
import com.company.skt.Skt;
import com.company.skt.lib.StageScreen;
import com.company.skt.lib.UpdateStage;
import com.company.skt.model.Assets;
import com.company.skt.model.Local;
import com.company.skt.model.SessionData;
import com.company.skt.view.*;

import java.io.IOException;

public class Menu extends StageScreen {
    
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
    
    public void buttonClicked(String clickCategories) {
        String category;
        String subCategory = "";
        if (clickCategories.contains("#")){
            category = clickCategories.substring(0, clickCategories.indexOf("#"));
            subCategory = clickCategories.substring(clickCategories.indexOf("#") + 1);
        }
        else{
            category = clickCategories;
        }

        switch(category) {
            // *** MAIN MENU-CLICKS ***
            case "HOST":
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                // TODO possibility to choose wether to start a new session or to load an saved one
                session = new HostSession();
                break;
            case "JOIN":
                DebugWindow.setUIFocus(DebugWindow.Focus.Lobby);
                session = new ClientSession();
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
                    mainMenuUI, Local.getString("dialog_question_quit"),
                    null, null, null, mainMenuUI, mainMenuUI,
                    () -> {
                        if(Skt.isDebug()) {
                            Skt.getDebugWindowPositionUpdater().shutdownNow();
                        }
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
                try {session.stopSession();} catch (IOException e) {e.printStackTrace();}
                setStageActive("lobbyUI", false);
                removeStage("lobbyUI");
                setStageActive("mainMenuUI", true);
                break;
            case "GAME_SETTINGS_CLICKED":
                switch (subCategory){
                    case "LOST_FACTOR":
                    data.setCfgValue("lost_factor", ((LobbyUI)findStage("lobbyUI")).lostFactorSelectBox.getSelected());
                        break;
                    case "RAMSCH":
                    data.setCfgValue("ramsch", String.valueOf(((LobbyUI)findStage("lobbyUI")).junkCheckbox.isChecked()));
                        break;
                    case "AMOUNT_GAMES":
                    data.setCfgValue("amount_games", ((LobbyUI)findStage("lobbyUI")).amountGamesSelectBox.getSelected());
                        break;
                    case "TIME_LIMIT":
                        switch(((LobbyUI)findStage("lobbyUI")).timeLimitSelectBox.getSelectedIndex()) {
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
            case "READY":
                // TODO getbuttonpressed
                break;
            case "KICK_PLAYER":

                switch (subCategory){
                    case "1":
                        // TODO KICKERIKII(player1)
                    case "2":
                        // TODO KICKERIKII(player2)
                }
                break;
            default :
                DebugWindow.println("buttonName " + clickCategories + " in " +
                                   this.getClass().getSimpleName() +  " not found");
        }
    }

    public void event(String eventCategories) {
        String category = "";
        String subCategory = "";
        if (eventCategories.contains("#")){
            category = eventCategories.substring(0, eventCategories.indexOf("#"));
            subCategory = eventCategories.substring(eventCategories.indexOf("#") + 1);
        }
        else{
            category = eventCategories;
        }
        switch(category) {
            case "READY_FOR_LOBBY":
                DebugWindow.println("[Menu|Event] ready for lobby");
                Gdx.app.postRunnable(() -> {
                    addStage(new LobbyUI("lobbyUI", true));
                    setStageActive("mainMenuUI", false);
                    ((LobbyUI)findStage("lobbyUI")).updateUI();
                });
                break;
            case "READY_FOR_SUMMARY":
                DebugWindow.println("[Menu|Event] ready for summary");
                // TODO open SummaryUI
                break;
            case "SESSION_DATA_CHANGED":
                DebugWindow.println("[Menu|Event] session data changed");
                if(SessionData.isHost() && session != null) {
                    System.out.println(SessionData.getDataStringForClient());
                    ((HostSession)session).sendStringToAll(SessionData.getDataStringForClient());
                }
                if(findStage("lobbyUI") != null) {
                    ((LobbyUI)findStage("lobbyUI")).updateUI();
                }
                if(Skt.isDebug()) {
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
                try {
                    if(SessionData.isHost()) {
                        // TODO prompt to save Session
                    }
                    session.stopSession();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
