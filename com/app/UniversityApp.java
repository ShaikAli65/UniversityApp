/*
 * Copyright (c) 2023, I I I T KOTTAYAM and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * I I I T Kottayam, Academics Department, 1st floor, Kottayam, 686635 INDIA.
 *
 * Please contact I I I T Kottayam, KERALA, KOTTAYAM 686635 INDIA.
 * or visit https://www.iiitkottayam.ac.in if you need additional information or have any
 * questions.
 */

package app;

import app.academics.AcademicsApp;
import app.admin.AdminApp;
import app.faculty.FacultyApp;
import app.student.StudentApp;
import db.CourseDB;
import db.FacultyDB;
import db.Loader;
import db.StudentDB;


public class UniversityApp implements University {
    public static final String Name = "I I I T - K";
    AdminApp admin = null;
    AcademicsApp academics = null;
    FacultyApp facultyApp = null;
    StudentApp student = null;

    UniversityApp() {
        System.out.println("\n############################\n");
        System.out.println("\n\u001B[32m  WELCOME TO " + Name + "  \u001B[0m\n");
        System.out.println("\n############################\n");
    }

    public static void main(String[] ar) {
        //   Loader.dupData();
        //   Loader.storeDataBases();
        UniversityApp.makeClear();
        Loader.loadDataBases();
        System.out.println("\nlaunching");

        try {
            UniversityApp university = new UniversityApp();
            university.display();
            System.out.println("Saving Data...");
            System.out.println("Data Saved Successfully");
        } finally {
            Loader.storeDataBases();
        }
    }

    @Override
    public String display() {
        loop:
        while (true) {
            this.printHeader("Home");
            this.showUniversityMenu();
            switch (University.getKeyPress()) {
                case 1 -> this.AdminContext();
                case 2 -> this.AcademicsContext();
                case 3 -> this.FacultyContext();
                case 4 -> this.StudentContext();
                case 0 -> {
                    break loop;
                }
                default -> getError(6);
            }
            makeClear();
        }
        return "";
    }

    // Context Functions

    private void AdminContext() {
        makeClear();
        if (admin == null) {
            admin = new AdminApp();
        } else {
            admin.display();
        }

    }

    private void AcademicsContext() {
        makeClear();
        if (academics == null) {
            academics = new AcademicsApp();
        } else {
            academics.display();
        }
    }

    private void FacultyContext() {
        if (FacultyDB.isEmpty() || StudentDB.isEmpty() || CourseDB.isEmpty()) {
            System.out.println("Admin & Academics should Make sure that all fields are at least filled");
            holdNextSlide();
            return;
        }
        makeClear();
        if (facultyApp == null) {
            facultyApp = new FacultyApp();
        } else {
            facultyApp.display();
        }
    }

    private void StudentContext() {
        if (FacultyDB.isEmpty() || StudentDB.isEmpty() || CourseDB.isEmpty()) {
            System.out.println("Academics && Admin should Make sure that all fields are at least filled");
            holdNextSlide();
            return;
        }
        makeClear();
        if (student == null) {
            student = new StudentApp();
        } else {
            student.display();
        }
    }

    // Utility Functions

    void showUniversityMenu() {
        System.out.print("""
                Choose :\s
                \t\t 1. Admin User
                \t\t 2. Academics User
                \t\t 3. Faculty User
                \t\t 4. Student User
                \t\t 0. Exit
                \t:"""
        );
    }

    public static void getError(int error_code) {
        switch (error_code) {
            case 1 -> System.out.println("\n\u001B[31m#ERROR://\u001B[0mEnter An Authentic Input .. error code 001\n");

            case 2 ->
                    System.out.println("\n\u001B[31m#ERROR://\u001B[0mCan't Authenticate you Sorry... error code 002\n");

            case 3 -> {
                System.out.println("\n\u001B[31m#ERROR://\u001B[0mNo Student-Courses Found... error code 003\n");
                holdNextSlide();
            }
            case 4 -> {
                System.out.println("\n\u001B[31m#ERROR://\u001B[0mNo Faculty-Courses Found... error code 004\n");
                holdNextSlide();
            }
            case 5 -> System.out.println("\n\u001B[31m#ERROR://\u001B[0mNo Sessions Found... error code 005\n");
            case 6 -> System.out.println("\n\t\u001B[31mERROR://\u001B[0m INVALID CHOICE error code 006\t\n");

            case 7 -> System.out.println("\u001B[31mERROR://\u001B[0mNot a valid input error code 007");
            case 9 -> {
                System.out.println("\u001B[31mERROR://\u001B[0mLooks like data does not contain any Student  error code 009");
                holdNextSlide();
            }
            case 10 -> {
                System.out.println("\u001B[31mERROR://\u001B[0mLooks like data does not contain any Faculty  error code 010");
                holdNextSlide();
            }
            case 11 -> {
                System.out.println("\u001B[31mERROR://\u001B[0m Nothing to display");
                holdNextSlide();
            }
            case 12 -> {
                System.out.println("\u001B[31mERROR://\u001B[0mAdd courses first");
                holdNextSlide();
            }

            case 13 -> System.out.println("\u001B[31mERROR://\u001B[0mIncorrect Password");

            case 15 -> {
                System.out.println("\u001B[31mERROR://Authentication Failed\u001B[0m");
                holdNextSlide();
            }

            case 16 -> System.out.println("\u001B[31mERROR://\u001B[0mNot a Valid date, defaulting... error code 016\n");

            case 17 -> System.out.println("\u001B[31mERROR://\u001B[0mNo exams are scheduled.. error code 017\n");
            case 18 -> {
                System.out.println("\u001B[31mERROR://\u001B[0mNo matched courses were found error code 018\n");
                holdNextSlide();
            }
            case 19 -> {
                System.out.println("\u001B[31mERROR://\u001B[0mNo sessions were found error code 019\n");
                holdNextSlide();
            }
            default -> System.out.println("\u001B[31m//UNDEFINED ERROR//\u001B[0m");
        }
    }

    public static void makeClear() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ignored) {
        }
    }

    public static void holdNextSlide() {
        System.out.print("\nPress Enter to Continue");
        scanner.next();
    }

    @Override
    public void printHeader(String s) {
        System.out.println("--------------University Panel--------------");
        System.out.println("IN: " + s);
        System.out.println("---------------------------------------------\n");
    }

}