@echo off
REM Script para executar GC Test com logging
REM Atividade 1.3 - Interpretando GC Logs

echo ========================================
echo GC Test Application - Atividade 1.3
echo ========================================
echo.

REM Compilar a classe
echo [1/3] Compilando GCTestApplication...
javac src\main\java\br\edu\iftm\estruturas\benchmark\GCTestApplication.java
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao
    pause
    exit /b 1
)

echo.
echo [2/3] Limpando gc.log anterior...
if exist gc.log del gc.log

echo.
echo [3/3] Executando GC Test com flags de logging...
echo Saída será registrada em gc.log
echo Pressione CTRL+C para interromper quando quiser
echo.

REM Executar com flags de GC logging
REM -Xms256m -Xmx256m: Define heap mínimo e máximo
REM -Xlog:gc*: Log todos os eventos de GC
REM file=gc.log: Arquivo de saída
REM time,uptime: Inclui timestamp e uptime no log
java -Xms256m -Xmx256m -Xlog:gc*:file=gc.log:time,uptime ^
    -cp src\main\java ^
    br.edu.iftm.estruturas.benchmark.GCTestApplication

echo.
echo [CONCLUIDO] GC log gerado em: gc.log
echo.
echo Para analisar o log, abra gc.log ou execute:
echo   type gc.log
echo.
pause
