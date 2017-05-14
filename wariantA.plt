set terminal png size 800,600
set o 'RelaxationZad1.png'
set xl 'os X'
set yl 'os Y'
set view map
set size ratio -1
set title 'Zad2. Przeplyw potencjalny'
set contours
set contour base
set cntrparam levels 30
unset clabel
set pm3d
splot 'warA_pot.dat' u 1:2:3 w pm3d lt -1 t ''

set terminal png size 800,600
set o 'RelaxationZad2.png'
set xl 'os X'
set yl 'os Y'
set view map
set size ratio -1
set title 'Zad2. Przeplyw potencjalny'
set contours
set contour base
set cntrparam levels 30
unset clabel
set pm3d
splot 'warA_pot2.dat' u 1:2:3 w pm3d lt -1 t ''


set terminal png size 800,600
set o 'IntegralZad1.png'
set xl 'iteracja'
set yl 'wrtosc calki'
set size ratio -1
unset pm3d
set title 'Wartosc calki w kolejnych iteracjach'
set log x
plot 'warA_integral.dat' with line

set terminal png size 800,600
set o 'IntegralZad2.png'
set xl 'iteracja'
set yl 'wrtosc calki'
set size ratio -1
unset pm3d
set title 'Wartosc calki w kolejnych iteracjach'
set log x
plot 'warA_integral2.dat' with line