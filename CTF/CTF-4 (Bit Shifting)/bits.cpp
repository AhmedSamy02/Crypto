#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
using namespace std;
int main()
{
    ifstream file("bits.txt");
    ofstream out("shifted.txt");
    string line;
    while (getline(file, line))
    {
        for (auto &&charachter : line)
        {
            out << char(charachter << 1);
        }
    }
    return 0;
}

