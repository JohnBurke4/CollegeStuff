LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

entity ripple_adder_16bit is
	port(
		carry_in : in std_logic;
		x : in std_logic_vector(15 downto 0);
		y : in std_logic_vector(15 downto 0);
		output : out std_logic_vector(15 downto 0);	
		carry_out : out std_logic
	);
end ripple_adder_16bit;

architecture behavior of ripple_adder_16bit is

component full_adder
port(
	x, y, carry_in : in std_logic;
	output : out std_logic;
	carry_out : out std_logic
);
end component;

signal carry_1, carry_2, carry_3, carry_4: std_logic;
signal carry_5, carry_6, carry_7, carry_8: std_logic;
signal carry_9, carry_10, carry_11, carry_12: std_logic;
signal carry_13, carry_14, carry_15 : std_logic;

constant prop_delay : time := 1 ns;

begin

	full_adder_0: full_adder port map(
		x => x(0),
		y => y(0),
		carry_in => carry_in,
		carry_out => carry_1,
		output => output(0)
	);
	
	full_adder_1: full_adder port map(
		x => x(1),
		y => y(1),
		carry_in => carry_1,
		carry_out => carry_2,
		output => output(1)
	);
	
	full_adder_2: full_adder port map(
		x => x(2),
		y => y(2),
		carry_in => carry_2,
		carry_out => carry_3,
		output => output(2)
	);
	
	full_adder_3: full_adder port map(
		x => x(3),
		y => y(3),
		carry_in => carry_3,
		carry_out => carry_4,
		output => output(3)
	);
	
	full_adder_4: full_adder port map(
		x => x(4),
		y => y(4),
		carry_in => carry_4,
		carry_out => carry_5,
		output => output(4)
	);
	
	full_adder_5: full_adder port map(
		x => x(5),
		y => y(5),
		carry_in => carry_5,
		carry_out => carry_6,
		output => output(5)
	);
	
	full_adder_6: full_adder port map(
		x => x(6),
		y => y(6),
		carry_in => carry_6,
		carry_out => carry_7,
		output => output(6)
	);
	
	full_adder_7: full_adder port map(
		x => x(7),
		y => y(7),
		carry_in => carry_7,
		carry_out => carry_8,
		output => output(7)
	);
	
	full_adder_8: full_adder port map(
		x => x(8),
		y => y(8),
		carry_in => carry_8,
		carry_out => carry_9,
		output => output(8)
	);
	
	full_adder_9: full_adder port map(
		x => x(9),
		y => y(9),
		carry_in => carry_9,
		carry_out => carry_10,
		output => output(9)
	);
	
	full_adder_10: full_adder port map(
		x => x(10),
		y => y(10),
		carry_in => carry_10,
		carry_out => carry_11,
		output => output(10)
	);
	
	full_adder_11: full_adder port map(
		x => x(11),
		y => y(11),
		carry_in => carry_11,
		carry_out => carry_12,
		output => output(11)
	);
	
	full_adder_12: full_adder port map(
		x => x(12),
		y => y(12),
		carry_in => carry_12,
		carry_out => carry_13,
		output => output(12)
	);
	
	full_adder_13: full_adder port map(
		x => x(13),
		y => y(13),
		carry_in => carry_13,
		carry_out => carry_14,
		output => output(13)
	);
	
	full_adder_14: full_adder port map(
		x => x(14),
		y => y(14),
		carry_in => carry_14,
		carry_out => carry_15,
		output => output(14)
	);
	
	full_adder_15: full_adder port map(
		x => x(15),
		y => y(15),
		carry_in => carry_15,
		carry_out => carry_out,
		output => output(15)
	);
	
end behavior;