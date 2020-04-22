library ieee;
use ieee.std_logic_1164.all;

entity decoder_8_1bit is 
	port( 
		s0: in std_logic;
		s1: in std_logic;
		s2: in std_logic;
		out0: out std_logic; 
		out1: out std_logic; 
		out2: out std_logic; 
		out3: out std_logic; 
		out4: out std_logic; 
		out5: out std_logic; 
		out6: out std_logic; 
		out7: out std_logic
	); 
end decoder_8_1bit;

architecture behavioral of decoder_8_1bit is

constant prop_delay : time := 1 ns;

begin	

	out0 <= ( (not s2) and (not s1) and (not s0) ) after prop_delay;
	out1 <= ( (not s2) and (not s1) and (s0) ) after prop_delay;
	out2 <= ( (not s2) and (s1) and (not s0) ) after prop_delay;   
	out3 <= ( (not s2) and (s1) and (s0) ) after prop_delay;			
	out4 <= ( (s2) and (not s1) and (not s0) ) after prop_delay;
	out5 <= ( (s2) and (not s1) and (s0) ) after prop_delay;
	out6 <= ( (s2) and (s1) and (not s0) ) after prop_delay;   
	out7 <= ( (s2) and (s1) and (s0) ) after prop_delay;

end behavioral; 	