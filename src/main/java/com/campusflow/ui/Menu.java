package com.campusflow.ui;

public final class Menu {

    private Menu() {
    }

    public static void header(String title) {
        System.out.println("\n==============================================");
        System.out.println("              " + title);
        System.out.println("==============================================");
    }
}
