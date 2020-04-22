library ieee;
use ieee.std_logic_1164.all;
  
entity register_file_tb is
end register_file_tb;
 
architecture behavior of register_file_tb is 
 
-- Component Declaration for the Unit Under Test (UUT)
 
component register_file
    port( 
		source_a : in std_logic_vector(2 downto 0);
		source_a_extra : in std_logic;
		source_b : in std_logic_vector(2 downto 0);
		source_b_extra : in std_logic;
		dest : in std_logic_vector(2 downto 0);
		dest_extra : in std_logic;
		register_write : in std_logic;
		clk : in std_logic;
		data : in std_logic_vector(15 downto 0);
		datapath_a_out : out std_logic_vector(15 downto 0);
		datapath_b_out : out std_logic_vector(15 downto 0);
		reg0 : out std_logic_vector( 15 downto 0);
		reg1 : out std_logic_vector( 15 downto 0);
		reg2 : out std_logic_vector( 15 downto 0);
		reg3 : out std_logic_vector( 15 downto 0);
		reg4 : out std_logic_vector( 15 downto 0);
		reg5 : out std_logic_vector( 15 downto 0);
		reg6 : out std_logic_vector( 15 downto 0);
		reg7 : out std_logic_vector( 15 downto 0);
		reg8 : out std_logic_vector( 15 downto 0)
	);
end component;
    

--Inputs
signal register_write : std_logic;
signal source_a : std_logic_vector(2 downto 0);
signal source_a_extra : std_logic;
signal source_b : std_logic_vector(2 downto 0);
signal source_b_extra : std_logic;
signal dest : std_logic_vector(2 downto 0);
signal dest_extra : std_logic;
signal clk : std_logic := '0';
signal data : std_logic_vector(15 downto 0);
   
--Outputs
signal reg0 : std_logic_vector(15 downto 0);
signal reg1 : std_logic_vector(15 downto 0);
signal reg2 : std_logic_vector(15 downto 0);
signal reg3 : std_logic_vector(15 downto 0);
signal reg4 : std_logic_vector(15 downto 0);
signal reg5 : std_logic_vector(15 downto 0);
signal reg6 : std_logic_vector(15 downto 0);
signal reg7 : std_logic_vector(15 downto 0);
signal reg8 : std_logic_vector (15 downto 0);
signal datapath_a_out : std_logic_vector(15 downto 0);
signal datapath_b_out : std_logic_vector(15 downto 0);

--Clock
constant clk_period : time := 10 ns;
 
begin
 
	-- Instantiate the Unit Under Test (UUT)
	uut: register_file port map (
          register_write => register_write,
          source_a => source_a,
          source_a_extra => source_a_extra,
          source_b => source_b,
          source_b_extra => source_b_extra,
          dest => dest,
          dest_extra => dest_extra,
		  clk => clk,
		  data => data,
		  datapath_a_out => datapath_a_out,
		  datapath_b_out => datapath_b_out,
		  reg0 => reg0,
		  reg1 => reg1,
		  reg2 => reg2,
		  reg3 => reg3,
		  reg4 => reg4,
		  reg5 => reg5,
		  reg6 => reg6,
		  reg7 => reg7,
		  reg8 => reg8
        );

   stim_proc: process
   begin		

	  clk <= '0';
	  register_write <= '1';
	  source_a <= "000";
	  source_a_extra <= '1';
	  source_b <= "000";
	  source_b_extra <= '1';
	  dest <= "000";
	  dest_extra <= '1';
	  data <= "1111111111111111";
      
	  wait for clk_period;
	  
	  clk <= '1';
	  
	  wait for clk_period;

	  clk <= '0';
	  register_write <= '1';
	  source_a <= "000";
	  source_a_extra <= '0';
	  source_b <= "000";
	  source_b_extra <= '0';
	  dest <= "000";
	  dest_extra <= '0';
	  data <= "1010101010101010";
      
	  wait for clk_period;
	  
	  clk <= '1';
	  
	  wait for clk_period;

	  clk <= '0';
	  register_write <= '0';
	  source_a <= "000";
	  source_a_extra <= '0';
	  source_b <= "001";
	  source_b_extra <= '0';
	  dest <= "001";
	  dest_extra <= '0';
	  data <= "1010101010101010";
      
	  wait for clk_period;
	  
	  clk <= '1';
	  
	  wait for clk_period;

	  clk <= '0';
	  register_write <= '1';
	  source_a <= "000";
	  source_a_extra <= '0';
	  source_b <= "000";
	  source_b_extra <= '1';
	  dest <= "010";
	  dest_extra <= '0';
	  data <= "1010101010101010";
      
	  wait for clk_period;
	  
	  clk <= '1';
	  
	  wait for clk_period;
     
   end process;

end;