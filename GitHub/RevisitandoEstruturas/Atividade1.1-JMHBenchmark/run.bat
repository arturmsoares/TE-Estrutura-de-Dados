@echo off
REM Script para facilitar a execução do benchmark JMH
REM Atividade 1.1 - Benchmark Básico: HashMap vs TreeMap

echo ========================================
echo JMH Benchmark - Estruturas de Dados
echo ========================================
echo.

REM Verificar se Maven está disponível
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Maven não está instalado ou não está no PATH
    echo Instale Maven em: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Compilar e gerar o JAR
echo [1/2] Compilando projeto e gerando JAR executável...
echo.
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo ERRO: Falha na compilação
    pause
    exit /b 1
)

echo.
echo [2/2] Executando benchmarks...
echo.
java -jar target/benchmarks.jar

pause
