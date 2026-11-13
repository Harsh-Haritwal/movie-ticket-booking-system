@echo off
REM Movie Ticket Booking System - run with embedded Tomcat
setlocal
cd /d "%~dp0"

if not exist out mkdir out
echo Compiling...
javac -encoding UTF-8 -cp "lib\*" -sourcepath "src\main\java" -d out ^
  "src\main\java\com\movieticket\server\EmbeddedServer.java" ^
  "src\main\java\com\movieticket\servlet\HomeServlet.java" ^
  "src\main\java\com\movieticket\servlet\LoginServlet.java" ^
  "src\main\java\com\movieticket\servlet\RegisterServlet.java" ^
  "src\main\java\com\movieticket\servlet\LogoutServlet.java" ^
  "src\main\java\com\movieticket\servlet\MovieServlet.java" ^
  "src\main\java\com\movieticket\servlet\MovieDetailServlet.java" ^
  "src\main\java\com\movieticket\servlet\SeatServlet.java" ^
  "src\main\java\com\movieticket\servlet\BookingServlet.java" ^
  "src\main\java\com\movieticket\servlet\CancelServlet.java" ^
  "src\main\java\com\movieticket\servlet\MyBookingsServlet.java" ^
  "src\main\java\com\movieticket\servlet\AdminDashboardServlet.java" ^
  "src\main\java\com\movieticket\servlet\AdminMovieServlet.java" ^
  "src\main\java\com\movieticket\servlet\AdminMovieDeleteServlet.java" ^
  "src\main\java\com\movieticket\servlet\AdminShowServlet.java" ^
  "src\main\java\com\movieticket\servlet\ErrorServlet.java" ^
  "src\main\java\com\movieticket\filter\AuthFilter.java"
if errorlevel 1 (
  echo COMPILE FAILED
  pause
  exit /b 1
)

echo Starting server on http://localhost:8080/
start "" cmd /c "timeout /t 4 /nobreak >nul & start http://localhost:8080/"
java -cp "out;lib\*" com.movieticket.server.EmbeddedServer
pause
