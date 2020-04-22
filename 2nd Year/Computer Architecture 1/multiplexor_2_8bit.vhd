library ieee;
use ieee.std_logic_1164.all;

entity multiplexor_2_8bit is
  port( 
        s : in  std_logic;
        in0 : in  std_logic_vector (7 downto 0);
        in1 : in  std_logic_vector (7 downto 0);
        output : out  std_logic_vector (7 downto 0)
  );
end multiplexor_2_8bit;

architecture behavioral of multiplexor_2_8bit is

constant prop_delay : time := 1 ns;

begin
  output <= 
    in0 after prop_delay when s = '0' else
		in1 after prop_delay when s = '1' else 
    "00000000" after prop_delay;
    
end behavioral;