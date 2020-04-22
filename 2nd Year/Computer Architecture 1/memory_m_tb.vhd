library ieee;
use ieee.std_logic_1164.all;
 
 
entity memory_m_tb is
end memory_m_tb;
 
architecture behavior of memory_m_tb is 
 
-- component declaration for the unit under test (uut)
 
component memory_m
  port( 
      memory_write : in std_logic;
		  clk : in std_logic;
		  data_in : in std_logic_vector(15 downto 0);
		  address : in std_logic_vector(15 downto 0);
      data_out : out std_logic_vector(15 downto 0)
  );
end component;

--Inputs
signal memory_write : std_logic;
signal clk : std_logic;
signal data_in: std_logic_vector (15 downto 0);
signal address : std_logic_vector (15 downto 0);

--Outputs
signal data_out: std_logic_vector (15 downto 0);

--Clock
constant clk_period : time := 10 ns;

begin 

	-- Instantiate the Unit Under Test (UUT)
  uut: memory_m port map (
      memory_write => memory_write,
		  clk => clk,
		  data_in => data_in,
      data_out => data_out,
      address => address
  );
		
	stim_proc: process
  begin

    memory_write <= '0';
	  clk <= '0';
	  address <= "0000000000000000";
		
    wait for clk_period;	
  
	  clk <= '1';
	
		wait for clk_period;	
      
	  clk <= '0';
	  memory_write <= '0';
	  address <= "0000000000000001";
		
    wait for clk_period;	
    
    clk <= '1';
	
    wait for clk_period;
    
    clk <= '0';
	  memory_write <= '1';
    address <= "0000000000000000";
    data_in <= "0000000000000000";
		
    wait for clk_period;	
    
    clk <= '1';
	
    wait for clk_period;
    
    clk <= '0';
	  memory_write <= '0';
	  address <= "0000000000000000";
		
    wait for clk_period;	
    
    clk <= '1';
	
	  wait for clk_period;
     
  end process;

end;