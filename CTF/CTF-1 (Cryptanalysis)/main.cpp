#include <iostream>
#include <fstream>
#include <map>
using namespace std;
map<char, char> dictionary;
// Function to Sort the map according to value
// The returned map is encrypted char --> decrypted char
// I started with frequencies according to https://en.wikipedia.org/wiki/Letter_frequency
// After that I manually changed the mappings to get the correct mappings by examining the decrypted text
// Until it gives me an understandable text

void sortAndExtractDictionary(map<char, int> &M)
{
    multimap<int, char> MM;

    for (auto &it : M)
    {
        MM.insert({it.second, it.first});
    }
    char freq[] = {'E', 'T', 'O', 'A', 'H', 'I', 'N', 'S', 'R', 'L', 'D', 'W', 'U', 'F', 'G', 'C', 'B', 'Y', 'P', 'M', 'K', 'V', 'J', 'Z', 'X', 'Q'};
    char i = 25;
    for (auto &it : MM)
    {
        dictionary[it.second] = freq[i];
        cout << it.second << " Mapped to : "
             << freq[i] << endl;
        i--;
    }
}
int main()
{
    map<char, int> freq;
    ifstream file("encrypted_text.txt");
    ofstream output("decrypted_text.txt");
    char ch;
    string text;
    while (file.get(ch))
    {
        text += ch;
        if (ch >= 'A' && ch <= 'Z')
        {
            freq[ch]++;
        }
    }

    sortAndExtractDictionary(freq);
    for (auto &it : text)
    {
        if (it >= 'A' && it <= 'Z')
        {
            output << dictionary[it];
        }
        else
        {
            output << it;
        }
    }
    file.close();
    output.close();
    return 0;
}