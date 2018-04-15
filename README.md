# Investment-Management-System
Final Year Project

Kevin Dervishi

1422334

K1502436

Supervisor: Kevin Lano


Full User Guide with images provided in appendices in project report.

Guide:

1. Setting Up Database
    
      The server must be running for the application to function.
    
        1. Download Oracle VirtualBox from: https://www.virtualbox.org/wiki/Downloads
        
        2. Once download is complete, click new to for a pop up to appear
        
        3. Select these values shown below
        
        4. select "Use an existing virtual hard disk file"
        
        5. open the virtual hard disk provided called "FYP User Database" and click
        create
      
        6. once created you will be sent back to the main window, select the server
        just created and click start
        
        7. When the server starts, it should log in automatically, if not, the password
        is "project1" that can be used to gain root access to the server
        
        8. Go back to VirtualBox main window and follow these intructions
        
            a. right click the project database and select "settings"
            
            b.  select netowork (left) and ensure network type is NAT then select port forwarding
            
            c. create a port forward from host IP to guest IP with port 3306 for both
                (host IP and guest IP may not be the same on your system and they may been
                to be modified to work)
        
        9. the server should run automatically when running the database but
            optionally you may select MySQL Workbench (from left side of screen 3rd
            item from top) to monitor the database
        
        10. select Local instance 3306
        
        11. Once inside, select server status (top left) and ensure the status is "RUNNING" (top right)
       
1. Running application
    
        1. Make sure your system has at least Java Runetime Environment (JRE) 9.
        source: http://www.oracle.com/technetwork/java/javase/downloads/jre9-
        downloads-3848532.html
        
        2. Connection to the internet should be established
        
        3. Open the app via the jar file included and test database connection by
        clicking sign in
        
        4. If database connection failed then an error message will appear (most
        likely port forwarding issue)
        
        5. Otherwise if the error message is "wrong username or password" then
        connection was successful and you can continue with the application
        6. You may log in with the pre-made account (username: "demo1" , pass-
        word: "demo1"). Otherwise follow the instructions below
        
        7. Start by clicking the "sign up" button then fill in the fields on screen
        
        8. On signing up you will be sent back to the sign in window, here you can
        input your username and password that were used during sign in
        
        9. The main investment management window will appear
        
        10. It is recommended to wait for the loading icon to disappear and data being
        loaded before continuing
        
        11. Start by depositing any amount of money into your account (top left)
        
        12. Now you may search for stock with the search bar on the left side of the
        screen
        
        13. Select any stock and click buy on the bottom right of the screen to purchase
        stocks (assuming enough funds)
        
        14. You may then sell a stock by selecting an investment from your portfolio
        (left) and then clicking the sell button
        
        15. Investment analysis can also be performed at any time (only works for the
        current stock being viewed)


Libraries

All libraries used are available for free and use is permitted under their respective copyright and licenses

licensing information can be found in the "libs" folder

List of libraries used:

* java-json (for reading and converting JSON to Java)

        author: Sun Microsystems Inc. 
        
        year: 2002
        
        source: https://mvnrepository.com/artifact/org.json/json
    
* mysql-connector-java (for interaction with MySQL Databases)
    
        author: Oracle and/or its affiliates
        
        year: 2017
        
        source: https://dev.mysql.com/downloads/connector/j/5.1.html

* c3p0 (for managing a connection pool)

        author: Machinery For Change, Inc.
            
        year: 2015
            
        source: http://www.mchange.com/projects/c3p0/

* StockChartsFX (for displaying a candlestick chart)

        author: Rob Terpilowski
    
        year: Unknown (at least 2014)
    
        source: https://github.com/rterp/StockChartsFX