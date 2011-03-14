#!/usr/bin/perl
use strict;
use warnings;

while(<>){
	my @parts = split /<>/;
	chomp($parts[0]);
	chomp($parts[1]);
	printf "%s<><a href=%s>%s</a>\n", $parts[1], $parts[0], $parts[1] ;
}
