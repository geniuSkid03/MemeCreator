package com.genius.memecreator.appDatas;

public class EditingMenus {

    private int menuIcon;
    private String menuName;
    private boolean isMenuSelected;

    public EditingMenus(int menuIcon, String menuName) {
        this.menuIcon = menuIcon;
        this.menuName = menuName;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public boolean isMenuSelected() {
        return isMenuSelected;
    }

    public void setMenuSelected(boolean menuSelected) {
        isMenuSelected = menuSelected;
    }
}
