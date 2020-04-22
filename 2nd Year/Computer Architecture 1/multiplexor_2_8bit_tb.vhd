library ieee;
use ieee.std_logic_1164.all;

entity multiplexor_2_8bit_tb IS
end multiplexor_2_8bit_tb;
 
architecture behavior of multiplexor_2_8bit_tb is 
 
    -- Component Declaration for the Unit Under Test (UUT)
 
    component multiplexor_2_8bit
    port( 
        s : in  std_logic;
        in0 : in  std_logic_vector (7 downto 0);
        in1 : in  std_logic_vector (7 downto 0);
        output : out  std_logic_vector (7 downto 0)
    );
    end component;
    

  --Inputs
  signal s : std_logic := '0';
  signal in0 : std_logic_vector(7 downto 0) := (others => '0');
  signal in1 : std_logic_vector(7 downto 0) := (others => '0');

 	--Outputs
  signal output : std_logic_vector(7 downto 0);

  --Clock
  constant clk_period : time := 10 ns;

 
begin
 
	-- Instantiate the Unit Under Test (UUT)
   uut: multiplexor_2_8bit port map (
          s => s,
          in0 => in0,
          in1 => in1,
          output => output
        );

   stim_proc: process
   begin

      in0 <= "10101010";
      in1 <= "11001100";
      s <= '0';
	  
      wait for clk_period;	

      s <= '1';
      
      wait for clk_period;
      
   end process;

end;
