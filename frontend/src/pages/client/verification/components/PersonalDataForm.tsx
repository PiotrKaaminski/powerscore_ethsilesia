import React, { FC, useEffect, useState } from 'react';
import { 
  TextField, 
  Button, 
  CircularProgress, 
  Grid,
  Box,
    Typography,
    Alert,
    MenuItem,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { PersonalDataRequest, ClientInfo } from '../../../../types/api';
import { addPersonalData } from '../../../../api/verification';

// List of ISO 3166-1 alpha-2 country codes
const ISO_COUNTRY_CODES = [
  'PL', 'US', 'DE', 'FR'
];

interface PersonalDataFormProps {
  verificationId: string;
  clientData?: ClientInfo;
}

const PersonalDataForm: FC<PersonalDataFormProps> = ({ 
  verificationId,
  clientData
}) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  const [showNotification, setShowNotification] = useState(false);
  const [countryCode, setCountryCode] = useState('+48');
  const [phoneNumberPart, setPhoneNumberPart] = useState('');
  const [formData, setFormData] = useState<PersonalDataRequest>({
    firstName: '',
    lastName: '',
    birthdate: '',
    nationality: 'PL',
    email: '',
    phoneNumber: '',
  });

  const mutation = useMutation({
    mutationFn: (data: PersonalDataRequest) => addPersonalData(verificationId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['verification', verificationId] });
      setShowNotification(true);
    },
    onError: () => {
      setShowNotification(true);
    },
  });

  useEffect(() => {
    if (showNotification) {
      const timer = setTimeout(() => {
        setShowNotification(false);
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [showNotification]);

  useEffect(() => {
    if (clientData) {
      const fullPhone = clientData.phoneNumber || '';
      // Try to split phone number (assuming it starts with + and code is 2-3 digits)
      // or just put everything in phoneNumberPart if it doesn't match a simple pattern
      if (fullPhone.startsWith('+')) {
        const parts = fullPhone.split(' ');
        if (parts.length > 1) {
          setCountryCode(parts[0]);
          setPhoneNumberPart(parts.slice(1).join(' '));
        } else {
          // Fallback: first 3-4 chars as country code
          setCountryCode(fullPhone.substring(0, 4));
          setPhoneNumberPart(fullPhone.substring(4));
        }
      } else {
        setPhoneNumberPart(fullPhone);
      }

      setFormData({
        firstName: clientData.firstName,
        lastName: clientData.lastName,
        birthdate: clientData.birthdate,
        nationality: clientData.nationality,
        email: clientData.email,
        phoneNumber: clientData.phoneNumber,
      });
    }
  }, [clientData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const finalPhoneNumber = `${countryCode}${phoneNumberPart}`.trim();
    mutation.mutate({ ...formData, phoneNumber: finalPhoneNumber });
  };

  const isReadOnly = !!clientData;
  const isPending = mutation.isPending;
  const isSuccess = mutation.isSuccess;
  const isError = mutation.isError;
  const errorMessage = (mutation.error as Error)?.message;

  return (
    <>
      <Typography variant="h4" gutterBottom align="center" sx={{ mb: 3 }}>
        {t('client.personalInfo.title')}
      </Typography>

      {isSuccess && showNotification && (
        <Alert severity="success" sx={{ mb: 3 }}>
          {t('client.personalInfo.success')}
        </Alert>
      )}

      {isError && showNotification && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {t('client.personalInfo.error', { message: errorMessage })}
        </Alert>
      )}

      <form onSubmit={handleSubmit}>
      <Grid container spacing={3}>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            label={t('client.personalInfo.firstName')}
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            disabled={isReadOnly}
            required
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            label={t('client.personalInfo.lastName')}
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            disabled={isReadOnly}
            required
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            label={t('client.personalInfo.birthdate')}
            name="birthdate"
            type="date"
            value={formData.birthdate}
            onChange={handleChange}
            disabled={isReadOnly}
            required
            slotProps={{
              inputLabel: { shrink: true }
            }}
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            select
            label={t('client.personalInfo.nationality')}
            name="nationality"
            value={formData.nationality}
            onChange={handleChange}
            disabled={isReadOnly}
            required
          >
            {ISO_COUNTRY_CODES.map((code) => (
              <MenuItem key={code} value={code}>
                {code}
              </MenuItem>
            ))}
          </TextField>
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            label={t('client.personalInfo.email')}
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            disabled={isReadOnly}
            required
          />
        </Grid>
        {isReadOnly ? (
          <Grid size={{ xs: 12, sm: 6 }}>
            <TextField
              fullWidth
              label={t('client.personalInfo.phoneNumber')}
              value={formData.phoneNumber}
              disabled={isReadOnly}
            />
          </Grid>
        ) : (
          <>
            <Grid size={{ xs: 12, sm: 2 }}>
              <TextField
                fullWidth
                label={t('client.personalInfo.countryCode')}
                placeholder="+48"
                value={countryCode}
                onChange={(e) => setCountryCode(e.target.value)}
                disabled={isReadOnly}
                required
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }}>
              <TextField
                fullWidth
                label={t('client.personalInfo.phoneNumber')}
                value={phoneNumberPart}
                onChange={(e) => setPhoneNumberPart(e.target.value)}
                disabled={isReadOnly}
                required
              />
            </Grid>
          </>
        )}
        
        {!isReadOnly && (
          <Grid size={{ xs: 12 }}>
            <Box sx={{ mt: 2, display: 'flex', justifyContent: 'flex-end' }}>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                size="large"
                disabled={isPending}
              >
                {isPending ? <CircularProgress size={24} color="inherit" /> : t('client.personalInfo.submit')}
              </Button>
            </Box>
          </Grid>
        )}
      </Grid>
      </form>
    </>
  );
};

export default PersonalDataForm;
