library ieee;
use ieee.std_logic_1164.all;
 
 
entity control_memory_tb is
end control_memory_tb;
 
architecture behavior of control_memory_tb is 
component control_memory
port(
    MW : out std_logic;
    MM : out std_logic;
    RW : out std_logic;
    MD : out std_logic;
    FS : out std_logic_vector(4 downto 0);
    MB : out std_logic;
    TB : out std_logic;
    TA : out std_logic;
    TD : out std_logic;
    PL : out std_logic;
    PI : out std_logic;
    IL : out std_logic;
    MC : out std_logic;
    MS : out std_logic_vector(2 downto 0);
    NA : out std_logic_vector(7 downto 0);
    input : in std_logic_vector(7 downto 0);
    output : out std_logic_vector(27 downto 0)
);
end component;

--Inputs
signal input: std_logic_vector(7 downto 0);

--Outputs
signal NA: std_logic_vector(7 downto 0); 
signal MS: std_logic_vector(2 downto 0); 
signal MC: std_logic; 
signal IL: std_logic; 
signal PI: std_logic; 
signal PL: std_logic; 
signal TA: std_logic; 
signal TB: std_logic; 
signal TD: std_logic; 
signal MB: std_logic; 
signal FS: std_logic_vector (4 downto 0); 
signal MD: std_logic; 
signal RW: std_logic; 
signal MM: std_logic; 
signal MW: std_logic;
signal output: std_logic_vector(27 downto 0);

--Clock
constant clk_period : time := 10 ns;


begin 

uut: control_memory PORT MAP (
			MW => MW,
            MM => MM,
			RW => RW,    
			MD => MD,
			FS => FS,
			MB => MB,
			TB => TB,
			TA => TA,
			TD => TD,
			PL => PL,
			PI => PI,
			IL => IL,
			MC => MC,
			MS => MS,
			NA => NA,
			input => input,
			output => output
);

stim_proc: process
begin		
    	input <= "00000000";
	
		wait for clk_period;

        input <= "00000001";
        
        wait for clk_period;
     
   end process;


end;

 