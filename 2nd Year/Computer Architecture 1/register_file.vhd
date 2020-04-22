library ieee;
use ieee.std_logic_1164.all;

entity register_file is 
	port( 
		source_a : in std_logic_vector(2 downto 0);
		source_a_extra : in std_logic;
		source_b : in std_logic_vector(2 downto 0);
		source_b_extra : in std_logic;
		dest : in std_logic_vector(2 downto 0);
		dest_extra : in std_logic;
		register_write : in std_logic;
		clk : in std_logic;
		data : in std_logic_vector( 15 downto 0);
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
end register_file;	

architecture behavioral of register_file is

	component register_16bit
	port(
		input: in std_logic_vector(15 downto 0);
		load, clk : in std_logic;
		output: out std_logic_vector(15 downto 0)
	);
	end component;
	
	component decoder_8_1bit
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
	end component;
	
	component multiplexor_2_16bit
	port( 
        s : in  std_logic;
        in0 : in  std_logic_vector (15 downto 0);
        in1 : in  std_logic_vector (15 downto 0);
        output : out  std_logic_vector (15 downto 0)
	);
	end component;
	
	component multiplexor_8_16bit
	port(
		s0, s1, s2 : in std_logic;
        in0 : in  std_logic_vector (15 downto 0);
    	in1 : in  std_logic_vector (15 downto 0);
    	in2 : in  std_logic_vector (15 downto 0);
    	in3 : in  std_logic_vector (15 downto 0);
		in4 : in  std_logic_vector (15 downto 0);
		in5 : in  std_logic_vector (15 downto 0);
		in6 : in  std_logic_vector (15 downto 0);
		in7 : in  std_logic_vector (15 downto 0);
		output : out  std_logic_vector (15 downto 0)
	);
	end component;

	signal decoder_out_0, decoder_out_1, decoder_out_2, decoder_out_3 : std_logic;
	signal decoder_out_4, decoder_out_5, decoder_out_6, decoder_out_7 : std_logic;
	signal decoder_out_8 : std_logic;
	
	signal load_register_0, load_register_1, load_register_2, load_register_3 : std_logic; 
	signal load_register_4, load_register_5, load_register_6, load_register_7 : std_logic; 
	signal load_register_8 : std_logic;

	signal register_0_value, register_1_value, register_2_value, register_3_value : std_logic_vector(15 downto 0); 
	signal register_4_value, register_5_value, register_6_value, register_7_value : std_logic_vector(15 downto 0); 
	signal register_8_value : std_logic_vector(15 downto 0);

	signal a_select_out : std_logic_vector(15 downto 0);
	signal b_select_out : std_logic_vector(15 downto 0);

	constant prop_delay : time := 1 ns;

begin
	
	register_0: register_16bit port map(
		input => data,
		load => load_register_0,
		clk => clk,
		output => register_0_value
	);
	
	register_1: register_16bit port map(
		input => data,
		load => load_register_1,
		clk => clk,
		output => register_1_value
	);
	
	register_2: register_16bit port map(
		input => data,
		load => load_register_2,
		clk => clk,
		output => register_2_value
	);
	
	register_3: register_16bit port map(
		input => data,
		load => load_register_3,
		clk => clk,
		output => register_3_value
	);
	
	register_4: register_16bit port map(
		input => data,
		load => load_register_4,
		clk => clk,
		output => register_4_value
	);
	
	register_5: register_16bit port map(
		input => data,
		load => load_register_5,
		clk => clk,
		output => register_5_value
	);
	
	register_6: register_16bit port map(
		input => data,
		load => load_register_6,
		clk => clk,
		output => register_6_value
	);
	
	register_7: register_16bit port map(
		input => data,
		load => load_register_7,
		clk => clk,
		output => register_7_value
	);
	
	register_8: register_16bit port map(
		input => data,
		load =>  load_register_8,
		clk => clk,
		output => register_8_value
	);
	
	decoder_destination_select: decoder_8_1bit port map(
		s0 => dest(0),
		s1 => dest(1),
		s2 => dest(2),
		out0 => decoder_out_0,
		out1 => decoder_out_1,
		out2 => decoder_out_2,
		out3 => decoder_out_3,
		out4 => decoder_out_4,
		out5 => decoder_out_5,
		out6 => decoder_out_6,
		out7 => decoder_out_7
	);
	
	multiplexor_a_select: multiplexor_8_16bit port map(
		s0 => source_a(0),
		s1 => source_a(1),
		s2 => source_a(2),
		in0 => register_0_value,
		in1 => register_1_value,
		in2 => register_2_value,
		in3 => register_3_value,
		in4 => register_4_value,
		in5 => register_5_value,
		in6 => register_6_value,
		in7 => register_7_value,
		output => a_select_out
	);
	
	multiplexor_b_select: multiplexor_8_16bit port map(
		s0 => source_b(0),
		s1 => source_b(1),
		s2 => source_b(2),
		in0 => register_0_value,
		in1 => register_1_value,
		in2 => register_2_value,
		in3 => register_3_value,
		in4 => register_4_value,
		in5 => register_5_value,
		in6 => register_6_value,
		in7 => register_7_value,
		output => b_select_out
	);

	multiplexor_a_extra: multiplexor_2_16bit port map(
		s => source_a_extra,
		in0 => a_select_out,
		in1 => register_8_value,
		output => datapath_a_out
	);

	multiplexor_b_extra: multiplexor_2_16bit port map(
		s => source_b_extra,
		in0 => b_select_out,
		in1 => register_8_value,
		output => datapath_b_out
	);
	
	reg0 <= register_0_value;
	reg1 <= register_1_value;
	reg2 <= register_2_value;
	reg3 <= register_3_value;
	reg4 <= register_4_value;
	reg5 <= register_5_value;
	reg6 <= register_6_value;
	reg7 <= register_7_value;
	reg8 <= register_8_value;
	
	load_register_0 <= decoder_out_0 and register_write and ( not dest_extra ) after prop_delay;
	load_register_1 <= decoder_out_1 and register_write and ( not dest_extra ) after prop_delay;
	load_register_2 <= decoder_out_2 and register_write and ( not dest_extra ) after prop_delay;
	load_register_3 <= decoder_out_3 and register_write and ( not dest_extra ) after prop_delay;
	load_register_4 <= decoder_out_4 and register_write and ( not dest_extra ) after prop_delay;
	load_register_5 <= decoder_out_5 and register_write and ( not dest_extra ) after prop_delay;
	load_register_6 <= decoder_out_6 and register_write and ( not dest_extra ) after prop_delay;
	load_register_7 <= decoder_out_7 and register_write and ( not dest_extra ) after prop_delay;
	load_register_8 <= dest_extra and register_write after prop_delay;
	
end behavioral;