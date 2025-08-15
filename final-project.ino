# define PICO_WIRELESS_BOARD

//libaries 
#include <DHT11.h>  
#include <LiquidCrystal_I2C.h> 
# include <stdio.h>
# include <stdlib.h>
# include "pico.h"
# include "pico/platform/cpu_regs.h"
# include "pico/stdlib.h"
# include "pico/multicore.h"
# include "hardware/timer.h"
# include "hardware/irq.h"
# include "pico/printf.h"
# include "pico/double.h"


# ifdef PICO_WIRELESS_BOARD
# include "cyw43.h"
# include "pico/cyw43_arch.h"
# endif

//constants 
#define BUZZER_PIN 6
# define LED_PIN_PICOW_BOARD 0 
# define DHT11_PIN 15
# define BLINKING_DELAY 100 //(in ms)


//constructors and objects  
LiquidCrystal_I2C lcd(0x27,16,2); 
DHT11 my_dht11(DHT11_PIN);  

//global variables
int current_temp; 
float time_start = 0.0; 
float elasped_time = 0.0; 

//=============================================================================
// Core 0 Function
//=============================================================================

/* 
@method isTempHigh prints to LCD screen if temperature is too high 
@param temp -> takes temperature from DHT11 
*/
void isTempHigh(int temp) { 
  # ifdef PICO_WIRELESS_BOARD
  if (temp >= 35){ 
    lcd.setCursor(0,1); 
    lcd.print("Temp too high!"); 
    } else if (temp < 35) { 
      lcd.setCursor(0,1); 
      lcd.print("                "); 
    }
    #endif
  }


//=============================================================================
// Core 1 Function
//=============================================================================

/*
@method blinkAlert blinks LED if temp is too high, if it returns to < 35, turn LED off 
@param temp -> takes temperaturefrom DHT11 
*/
void blinkAlert(int temp){ 
  # ifdef PICO_WIRELESS_BOARD
  if (temp >= 35){ 
    tone(BUZZER_PIN, 1000);
    cyw43_arch_gpio_put(LED_PIN_PICOW_BOARD, HIGH);
    delay(BLINKING_DELAY);
    noTone(BUZZER_PIN); 
    cyw43_arch_gpio_put(LED_PIN_PICOW_BOARD, LOW);
    } else if (temp < 35) { 
      cyw43_arch_gpio_put(LED_PIN_PICOW_BOARD, LOW);
    }
    #endif
  }



//=============================================================================
// Core 0 Setup 
//=============================================================================
//initialize LCD, DHT11 and LED
void setup(){ 
  Serial.begin(9600); 
  pinMode(BUZZER_PIN, OUTPUT);
  lcd.init(); 
  lcd.backlight(); 
  lcd.setCursor(0,0);
  lcd.print("Ready");
  delay(1500); 
  lcd.clear(); 

  //wait for serial connection 
  while (!Serial.availableForWrite()) {
    delay(100);
    }
  
  time_start = millis(); 

}


//=============================================================================
// Core 0 Loop 
//=============================================================================
//gets a temperature reading and the time that it occurrs and outputs to the serial port 
void loop() { 
  current_temp = my_dht11.readTemperature(); 
  elasped_time = (millis() -  time_start) / 1000; 

  Serial.print(elasped_time); 
  Serial.print(","); 
  Serial.println(current_temp); 


  //output temperature to LCD
  lcd.setCursor(0,0);
  lcd.print("Temp: "); 
  lcd.print(current_temp); 
  lcd.write(byte(223));
  lcd.print("C");

  //check if this temp reading is > 34
  isTempHigh(current_temp); 
  delay(1000);

}

//=============================================================================
// Core 1 Setup
//=============================================================================
void setup1(){ 
 # ifdef PICO_WIRELESS_BOARD 
  if (cyw43_arch_init()) {
    while(true) {
      tight_loop_contents();
      }
    } 
 # endif   
} 

//=============================================================================
// Core 1 Loop
//=============================================================================
//calls the LED function to see if temp is too high 
void loop1() { 
 # ifdef PICO_WIRELESS_BOARD
 blinkAlert(current_temp); 
 delay(1000); 
  
#endif 
 
}











