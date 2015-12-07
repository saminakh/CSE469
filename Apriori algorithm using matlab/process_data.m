function [ radiant_data, dire_data ] = process_data()
%PROCESS_DATA Summary of this function goes here
%   Detailed explanation goes here
fileID = 'match_ids.txt';
preprocessed_data = dlmread(fileID,' ');
preprocessed_data = preprocessed_data(:, 2:11);

radiant_preprocessed = preprocessed_data(:, 1:5);
dire_preprocessed = preprocessed_data(:, 6:10);

num_samples = size(radiant_preprocessed, 1);
radiant_data = zeros(num_samples, 112);
dire_data = zeros(num_samples, 112);

for i = 1:num_samples
    for j=1:5
        temp_radiant = radiant_preprocessed(i,j);
        temp_dire = dire_preprocessed(i,j);
        
        radiant_data(i, temp_radiant) = 1;
        dire_data(i, temp_dire) = 1;  
    end
end


end

